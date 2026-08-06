package com.isaacshub.app.vault.backup

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import java.io.File

/**
 * Discovers and backs up all preference DataStores in the app dynamically,
 * so new tools automatically get backup coverage without code changes.
 */

/** Get all preference DataStore files from the app's datastore directory. */
fun getAllPreferenceFiles(context: Context): List<File> {
    val datastoreDir = File(context.filesDir, "datastore")
    if (!datastoreDir.exists()) return emptyList()

    return datastoreDir
        .listFiles { file -> file.name.endsWith(".preferences_pb") }
        ?.sortedByDescending { it.lastModified() }
        ?: emptyList()
}

/** Extract the DataStore name from the protobuf filename. */
fun getDataStoreNameFromFile(file: File): String {
    return file.name.removeSuffix(".preferences_pb")
}

/**
 * Convert all preferences from a single DataStore to JSON format.
 * Since we can't easily read .preferences_pb protobuf files directly,
 * we access preferences via the context's DataStore files.
 *
 * This serializes preferences in a generic way that works regardless
 * of DataStore name or new preferences added in the future.
 */
suspend fun preferencesFileToJson(preferences: Preferences): String {
    val json = JSONObject()
    for ((key, value) in preferences.asMap()) {
        json.put(key.name, if (value is Set<*>) {
            // Convert sets to JSON arrays (used for string sets)
            org.json.JSONArray(value.toList())
        } else {
            value
        })
    }
    return json.toString()
}

/**
 * Represents a complete backup of all app preferences.
 * Maps each DataStore name to its serialized JSON preferences.
 */
data class PreferencesBackupBundle(
    val timestamp: Long,
    val preferences: Map<String, String> // datastoreName -> jsonString
)

/**
 * Parse preference values from JSON, handling type conversions.
 * Since DataStore keys have specific types, we try to infer and convert values appropriately.
 */
fun parsePreferenceValue(value: Any?): Any? {
    return when (value) {
        is String -> value
        is Number -> when {
            value is Double || value is Float -> value.toDouble()
            value is Long -> value.toLong()
            else -> value.toInt()
        }
        is Boolean -> value
        is JSONObject -> {
            // For sets or complex objects, convert to list/string
            value.names()?.let { names ->
                mutableListOf<String>().apply {
                    for (i in 0 until (names?.length() ?: 0)) {
                        names?.getString(i)?.let { add(it) }
                    }
                }
            } ?: listOf<String>()
        }
        is org.json.JSONArray -> {
            // JSONArray items
            mutableListOf<String>().apply {
                for (i in 0 until value.length()) {
                    when (val item = value.opt(i)) {
                        is String -> add(item)
                        else -> add(item?.toString() ?: "")
                    }
                }
            }
        }
        else -> value?.toString()
    }
}
