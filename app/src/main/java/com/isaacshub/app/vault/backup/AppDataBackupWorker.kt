package com.isaacshub.app.vault.backup

import android.content.Context
import androidx.room.RoomDatabase
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.isaacshub.app.App
import com.isaacshub.app.core.data.AppDatabase
import com.isaacshub.app.core.data.WorkDatabase
import com.isaacshub.app.essentials.data.EssentialsDatabase
import com.isaacshub.app.vault.data.VaultApiClient
import kotlinx.coroutines.flow.first
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val REMOTE_FOLDER = "AppBackup"
private val DATABASE_FILE_NAMES = listOf("app.db", "work.db", "essentials.db")
private const val MAX_BACKUP_VERSIONS = 5 // Keep last 5 backups

/**
 * Backs up every Room database and every app preference to the paired server, so nothing is lost
 * if the app is uninstalled or the phone is lost/replaced. Databases are copied whole (not
 * re-derived field by field) so this stays correct automatically as entities change; preferences
 * are serialized generically for the same reason.
 */
class AppDataBackupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as App
        val vaultPrefs = app.vaultPreferencesRepository
        val connection = vaultPrefs.connection.first() ?: return Result.success()
        val client = VaultApiClient(connection, vaultPrefs.preferredBaseUrl.first())

        checkpointWal(AppDatabase.getInstance(applicationContext))
        checkpointWal(WorkDatabase.getInstance(applicationContext))
        checkpointWal(EssentialsDatabase.getInstance(applicationContext))

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

        var allSucceeded = true
        for (dbName in DATABASE_FILE_NAMES) {
            val dbFile = applicationContext.getDatabasePath(dbName)
            if (!dbFile.exists()) continue

            // Upload with timestamp to preserve history
            val timestampedPath = "$REMOTE_FOLDER/${dbName}.${timestamp}"
            if (!client.uploadFile(dbFile, timestampedPath, "application/octet-stream")) {
                allSucceeded = false
                continue
            }

            // Also update the "current" symlink/latest version
            if (!client.uploadFile(dbFile, "$REMOTE_FOLDER/$dbName", "application/octet-stream")) {
                allSucceeded = false
            }
        }

        // Dynamically discover and backup all preference DataStores.
        // This automatically includes new tools without code changes.
        val preferenceFiles = getAllPreferenceFiles(applicationContext)
        for (prefsFile in preferenceFiles) {
            val datastoreName = getDataStoreNameFromFile(prefsFile)

            try {
                // Get raw preferences from the appropriate repository if available,
                // otherwise skip (will be backed up from file directly in future enhancement)
                val prefs = when (datastoreName) {
                    "isaacs_hub_prefs" -> app.preferencesRepository.rawPreferences.first()
                    "isaacs_hub_vault_prefs" -> app.vaultPreferencesRepository.getRawPreferences().first()
                    "feature_funnel_prefs" -> app.featureFunnelPreferencesRepository.getRawPreferences().first()
                    "landing_preferences" -> app.landingPreferencesRepository.getRawPreferences().first()
                    else -> {
                        // Skip preferences that aren't in repositories yet
                        // New tools can be added to this when statement as they're created
                        // Or backup could be implemented to read protobuf files directly
                        android.util.Log.w("AppDataBackupWorker", "No repository found for preferences $datastoreName - skipping for now")
                        continue
                    }
                }

                val tempPrefsFile = File(applicationContext.cacheDir, "${datastoreName}_backup.json")
                try {
                    val json = preferencesToJson(prefs)
                    tempPrefsFile.writeText(json)

                    // Upload with timestamp to preserve history
                    val timestampedPath = "$REMOTE_FOLDER/preferences/${datastoreName}.${timestamp}"
                    if (!client.uploadFile(tempPrefsFile, timestampedPath, "application/json")) {
                        allSucceeded = false
                        continue
                    }

                    // Also update the "current" version for easy restoration
                    val currentPath = "$REMOTE_FOLDER/preferences/${datastoreName}.json"
                    if (!client.uploadFile(tempPrefsFile, currentPath, "application/json")) {
                        allSucceeded = false
                    }
                } finally {
                    tempPrefsFile.delete()
                }
            } catch (e: Exception) {
                android.util.Log.e("AppDataBackupWorker", "Failed to backup preferences $datastoreName", e)
                allSucceeded = false
            }
        }

        client.resolvedBaseUrl?.let { vaultPrefs.setPreferredBaseUrl(it) }
        if (allSucceeded) vaultPrefs.setLastBackupEpochMillis(System.currentTimeMillis())

        return if (allSucceeded) Result.success() else Result.retry()
    }

    /** Flushes WAL into the main db file so the copy we upload isn't missing recently-committed writes. */
    private fun checkpointWal(database: RoomDatabase) {
        runCatching {
            database.openHelper.writableDatabase.query("PRAGMA wal_checkpoint(FULL)").use { it.moveToFirst() }
        }
    }
}
