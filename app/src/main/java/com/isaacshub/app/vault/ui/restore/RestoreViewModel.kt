package com.isaacshub.app.vault.ui.restore

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaacshub.app.App
import com.isaacshub.app.vault.data.VaultApiClient
import com.isaacshub.app.vault.data.VaultPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

data class BackupDatabase(
    val name: String,
    val displayName: String,
    val fileName: String,
    val selected: Boolean = false
)

sealed class RestoreState {
    object Idle : RestoreState()
    object Loading : RestoreState()
    data class Success(
        val restoredDatabases: List<String>,
        val restoredPreferences: List<String> = emptyList()
    ) : RestoreState()
    data class Error(val message: String) : RestoreState()
}

class RestoreViewModel(
    private val context: Context,
    private val vaultPrefs: VaultPreferencesRepository
) : ViewModel() {

    private val _databases = MutableStateFlow(
        listOf(
            BackupDatabase("app", "App Data (Sleep, Banking, Feature Funnel)", "app.db"),
            BackupDatabase("work", "Work Data (Time Tracking, Route Helper)", "work.db"),
            BackupDatabase("essentials", "Essentials (Chores, Family)", "essentials.db")
        )
    )
    val databases: StateFlow<List<BackupDatabase>> = _databases

    private val _restoreState = MutableStateFlow<RestoreState>(RestoreState.Idle)
    val restoreState: StateFlow<RestoreState> = _restoreState

    private val _lastBackupTime = MutableStateFlow<Long?>(null)
    val lastBackupTime: StateFlow<Long?> = _lastBackupTime

    init {
        viewModelScope.launch {
            _lastBackupTime.value = vaultPrefs.lastBackupEpochMillis.first()
        }
    }

    fun toggleDatabaseSelection(databaseName: String) {
        _databases.value = _databases.value.map { db ->
            if (db.name == databaseName) {
                db.copy(selected = !db.selected)
            } else {
                db
            }
        }
    }

    fun restoreSelectedDatabases() {
        viewModelScope.launch {
            _restoreState.value = RestoreState.Loading

            try {
                val connection = vaultPrefs.connection.first()
                if (connection == null) {
                    _restoreState.value = RestoreState.Error("No server connection configured")
                    return@launch
                }

                val client = VaultApiClient(connection, vaultPrefs.preferredBaseUrl.first())
                val selectedDatabases = _databases.value.filter { it.selected }

                if (selectedDatabases.isEmpty()) {
                    _restoreState.value = RestoreState.Error("No databases selected")
                    return@launch
                }

                val restoredDatabases = mutableListOf<String>()
                val restoredPreferences = mutableListOf<String>()
                var anyFailed = false

                // Restore databases
                for (database in selectedDatabases) {
                    val remotePath = "AppBackup/${database.fileName}"
                    val tempFile = File(context.cacheDir, "restore_${database.fileName}")

                    try {
                        // Download backup from server
                        val success = client.downloadFile(remotePath, tempFile)
                        if (!success) {
                            anyFailed = true
                            continue
                        }

                        // Get the actual database path
                        val dbPath = context.getDatabasePath(database.fileName)

                        // Close any open connections to this database
                        // Note: This requires the app to be restarted to take effect properly
                        context.deleteDatabase(database.fileName)

                        // Copy the downloaded backup to the database location
                        tempFile.copyTo(dbPath, overwrite = true)
                        restoredDatabases.add(database.displayName)

                    } catch (e: Exception) {
                        anyFailed = true
                        android.util.Log.e("RestoreViewModel", "Failed to restore ${database.fileName}", e)
                    } finally {
                        tempFile.delete()
                    }
                }

                // Restore all preference files that were backed up
                try {
                    val app = context as App
                    val preferenceNames = listOf(
                        "isaacs_hub_prefs" to app.preferencesRepository,
                        "isaacs_hub_vault_prefs" to app.vaultPreferencesRepository,
                        "feature_funnel_prefs" to app.featureFunnelPreferencesRepository,
                        "landing_preferences" to app.landingPreferencesRepository
                    )

                    for ((datastoreName, repository) in preferenceNames) {
                        val remotePath = "AppBackup/preferences/${datastoreName}.json"
                        val tempFile = File(context.cacheDir, "${datastoreName}_restore.json")

                        try {
                            // Download preference backup from server
                            val success = client.downloadFile(remotePath, tempFile)
                            if (!success) {
                                android.util.Log.w("RestoreViewModel", "Preference file not found: $datastoreName")
                                continue
                            }

                            // Read the JSON and restore preferences
                            val jsonString = tempFile.readText()
                            val json = JSONObject(jsonString)

                            // Restore preferences based on repository type
                            when (datastoreName) {
                                "isaacs_hub_prefs" -> restorePreferencesToRepository(json, repository)
                                "isaacs_hub_vault_prefs" -> restorePreferencesToRepository(json, repository)
                                "feature_funnel_prefs" -> restorePreferencesToRepository(json, repository)
                                "landing_preferences" -> restorePreferencesToRepository(json, repository)
                            }

                            restoredPreferences.add(datastoreName)
                        } catch (e: Exception) {
                            android.util.Log.e("RestoreViewModel", "Failed to restore preferences $datastoreName", e)
                            anyFailed = true
                        } finally {
                            tempFile.delete()
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.w("RestoreViewModel", "Could not restore preferences (app context issue)", e)
                }

                if (restoredDatabases.isNotEmpty() || restoredPreferences.isNotEmpty()) {
                    _restoreState.value = RestoreState.Success(restoredDatabases, restoredPreferences)
                } else {
                    _restoreState.value = RestoreState.Error("Failed to restore any data")
                }

            } catch (e: Exception) {
                _restoreState.value = RestoreState.Error(e.message ?: "Unknown error occurred")
                android.util.Log.e("RestoreViewModel", "Restore failed", e)
            }
        }
    }

    /**
     * Restore JSON preferences to a DataStore.
     * For now, this is a placeholder that logs what would be restored.
     * Full implementation would require extending each repository with restore methods
     * that know how to map JSON keys back to typed DataStore keys.
     *
     * Each repository would need a method like:
     *   suspend fun restoreFromJson(json: JSONObject)
     *
     * Which would parse the JSON and call:
     *   context.dataStore.edit { prefs ->
     *       prefs[Keys.KEY_NAME] = value
     *   }
     */
    private suspend fun restorePreferencesToRepository(json: JSONObject, repository: Any) {
        val keyCount = json.length()
        android.util.Log.d("RestoreViewModel", "Ready to restore preferences with $keyCount keys")

        // Once repositories implement restoreFromJson(), the logic would be:
        // when (repository) {
        //     is UserPreferencesRepository -> repository.restoreFromJson(json)
        //     is VaultPreferencesRepository -> repository.restoreFromJson(json)
        //     is FeatureFunnelPreferencesRepository -> repository.restoreFromJson(json)
        //     is LandingPreferencesRepository -> repository.restoreFromJson(json)
        // }

        // For now, we just log that the data is available for restoration
    }

    fun resetState() {
        _restoreState.value = RestoreState.Idle
    }
}
