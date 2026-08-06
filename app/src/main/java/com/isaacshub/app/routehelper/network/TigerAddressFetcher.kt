package com.isaacshub.app.routehelper.network

import android.content.Context
import android.util.Log
import com.isaacshub.app.routehelper.domain.GeoPoint
import com.isaacshub.app.routehelper.domain.HouseNumberRange
import com.isaacshub.app.routehelper.domain.TigerAddressFeature
import com.isaacshub.app.routehelper.domain.filterAddressGroupsNearBuildings
import com.isaacshub.app.routehelper.domain.interpolateAddressGroups
import com.isaacshub.app.routehelper.domain.parseParity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

private const val TAG = "TigerAddressFetcher"
private const val USER_AGENT = "IsaacsHub/1.0 (personal route-building app)"
private const val TIGER_YEAR = "2023"

/**
 * Address source for Route Helper: Census TIGER/Line ADDRFEAT data, which covers every road in the
 * country with left/right house-number ranges - unlike OpenStreetMap, whose per-building address
 * tagging is only as complete as local volunteer mapping, and is near-empty in most rural counties.
 * Each county's ADDRFEAT shapefile (~1MB zipped) is downloaded once from census.gov and cached under
 * filesDir, so repeat ZIP lookups in the same county are instant and offline-capable afterward.
 */
class TigerAddressFetcher(private val context: Context) {

    private val geocoder = NominatimGeocoder()
    private val fipsLookup by lazy { CountyFipsLookup(context) }
    private val buildingFootprintFetcher = BuildingFootprintFetcher(context)

    /** Just the center point of a ZIP code, for zooming a map to it (e.g. testing mode) without a full address fetch. */
    suspend fun geocodeZip(zip: String): GeoPoint? = withContext(Dispatchers.IO) {
        geocoder.geocodeZip(zip)?.center
    }

    suspend fun fetchAddressesForZip(zip: String, skipBuildingFilter: Boolean = false): AddressFetchResult = withContext(Dispatchers.IO) {
        val location = geocoder.geocodeZip(zip)
            ?: return@withContext AddressFetchResult.Failure("Couldn't find that ZIP code")
        val fips = fipsLookup.lookup(location.stateAbbreviation, location.county)
            ?: return@withContext AddressFetchResult.Failure(
                "Couldn't map ${location.county}, ${location.stateAbbreviation} to a Census county"
            )

        // Catches Throwable (not just Exception) deliberately: parsing a large county's
        // shapefile can throw OutOfMemoryError on memory-constrained devices, and an OOM
        // here must NOT be allowed to propagate as an uncaught Error and kill the whole app -
        // it should surface as a normal, recoverable failure for this one route-creation attempt.
        val (dbfBytes, shpBytes) = try {
            loadCountyShapefile(fips.stateFp, fips.countyFp)
        } catch (e: OutOfMemoryError) {
            Log.e(TAG, "Out of memory loading county address data for ZIP $zip", e)
            return@withContext AddressFetchResult.Failure(
                "This county's address data is too large to process on this device"
            )
        } catch (e: Exception) {
            return@withContext AddressFetchResult.Failure("Couldn't download Census address data: ${e.message}")
        }

        val rawRecords = TigerDbfParser.parse(dbfBytes)
        val lines = TigerShpParser.parseLines(shpBytes)
        val addressGroups = rawRecords.zip(lines)
            .asSequence()
            .filter { (record, _) -> !record.isDeleted && (record.zipL == zip || record.zipR == zip) }
            .flatMap { (record, points) ->
                val feature = TigerAddressFeature(
                    streetName = record.fullName,
                    leftRange = HouseNumberRange(record.lFromHn.toIntOrNull(), record.lToHn.toIntOrNull(), parseParity(record.parityL)),
                    rightRange = HouseNumberRange(record.rFromHn.toIntOrNull(), record.rToHn.toIntOrNull(), parseParity(record.parityR)),
                    zipLeft = record.zipL,
                    zipRight = record.zipR,
                    vertices = points
                )
                interpolateAddressGroups(feature, zip)
            }
            .toList()

        // For Amazon routes, skip building proximity filter to get all possible addresses
        val finalAddresses = if (skipBuildingFilter) {
            val flattenedAddresses = addressGroups.flatten()
            Log.i(TAG, "Skipping building filter for ZIP $zip - returning all ${flattenedAddresses.size} interpolated addresses")
            flattenedAddresses
        } else {
            val buildingCentroids = buildingFootprintFetcher.fetchNearbyBuildingCentroids(location.center)
            if (buildingCentroids.isEmpty()) {
                Log.w(TAG, "No building footprints available for ZIP $zip - skipping phantom-address filter")
            }
            filterAddressGroupsNearBuildings(addressGroups, buildingCentroids)
        }

        AddressFetchResult.Success(finalAddresses.map { FetchedAddress(it.label, it.location) })
    }

    private fun loadCountyShapefile(stateFp: String, countyFp: String): Pair<ByteArray, ByteArray> {
        val cacheDir = File(context.filesDir, "tiger_cache").apply { mkdirs() }
        val fileName = "tl_${TIGER_YEAR}_$stateFp${countyFp}_addrfeat.zip"
        val zipFile = File(cacheDir, fileName)
        if (!zipFile.exists()) {
            downloadTo(zipFile, "https://www2.census.gov/geo/tiger/TIGER$TIGER_YEAR/ADDRFEAT/$fileName")
        }

        var dbfBytes: ByteArray? = null
        var shpBytes: ByteArray? = null
        ZipFile(zipFile).use { zip ->
            val entries = zip.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                when {
                    entry.name.endsWith(".dbf") -> dbfBytes = readEntryExact(zip, entry)
                    entry.name.endsWith(".shp") -> shpBytes = readEntryExact(zip, entry)
                }
            }
        }
        if (dbfBytes == null || shpBytes == null) {
            zipFile.delete()
        }
        return (dbfBytes ?: error("Missing .dbf in county address data")) to
            (shpBytes ?: error("Missing .shp in county address data"))
    }

    /**
     * Reads a zip entry into a byte array sized exactly to its known uncompressed length,
     * instead of the default InputStream.readBytes()/ByteArrayOutputStream growth strategy
     * (which doubles its backing array on overflow). For a large county's .shp file - which
     * can be 50-100+MB uncompressed for populous counties like Los Angeles - that doubling
     * needs close to 2x the final size in memory simultaneously (old + new backing arrays),
     * which reliably triggers OutOfMemoryError on the ~192-256MB heaps typical of
     * budget/older devices. Reading straight into a single correctly-sized array avoids that
     * doubling overhead entirely.
     */
    private fun readEntryExact(zip: ZipFile, entry: ZipEntry): ByteArray {
        val size = entry.size
        return zip.getInputStream(entry).use { input ->
            if (size < 0 || size > Int.MAX_VALUE) {
                // Central directory didn't report a usable size (rare) - fall back to the
                // growable read.
                input.readBytes()
            } else {
                val buffer = ByteArray(size.toInt())
                var offset = 0
                while (offset < buffer.size) {
                    val read = input.read(buffer, offset, buffer.size - offset)
                    if (read == -1) break
                    offset += read
                }
                buffer
            }
        }
    }

    private fun downloadTo(target: File, url: String) {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.setRequestProperty("User-Agent", USER_AGENT)
        conn.connectTimeout = 20_000
        conn.readTimeout = 60_000
        try {
            val code = conn.responseCode
            if (code != HttpURLConnection.HTTP_OK) {
                error("HTTP $code fetching county address data")
            }
            val tmp = File(target.parentFile, "${target.name}.part")
            conn.inputStream.use { input -> tmp.outputStream().use { output -> input.copyTo(output) } }
            tmp.renameTo(target)
        } finally {
            conn.disconnect()
        }
    }
}
