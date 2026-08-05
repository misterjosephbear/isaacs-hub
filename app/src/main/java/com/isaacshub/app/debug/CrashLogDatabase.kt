package com.isaacshub.app.debug

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.google.gson.Gson

/**
 * Entity for storing crash logs in local database.
 * Uses JSON serialization to store complex CrashLog objects.
 */
@Entity(tableName = "crash_logs")
data class CrashLogEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val appVersion: String,
    val crashJsonData: String  // Full CrashLog serialized as JSON
)

/**
 * DAO for crash log operations.
 */
@Dao
interface CrashLogDao {
    @Insert
    suspend fun insertCrash(crash: CrashLogEntity)

    @Query("SELECT * FROM crash_logs ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentCrashes(limit: Int = 5): List<CrashLogEntity>

    @Query("SELECT * FROM crash_logs WHERE id = :id")
    suspend fun getCrashById(id: String): CrashLogEntity?

    @Query("SELECT * FROM crash_logs WHERE appVersion = :version ORDER BY timestamp DESC")
    suspend fun getCrashesByVersion(version: String): List<CrashLogEntity>

    @Query("SELECT COUNT(*) FROM crash_logs")
    suspend fun getCrashCount(): Int

    @Delete
    suspend fun deleteCrash(crash: CrashLogEntity)

    @Query("DELETE FROM crash_logs WHERE id IN (SELECT id FROM crash_logs ORDER BY timestamp ASC LIMIT (SELECT COUNT(*) - 50 FROM crash_logs))")
    suspend fun deleteCrashesOlderThan50()
}

/**
 * Local database for storing crash logs.
 * Automatically maintains only the 50 most recent crashes.
 */
@Database(entities = [CrashLogEntity::class], version = 1, exportSchema = false)
abstract class CrashLogDatabaseImpl : RoomDatabase() {
    abstract fun crashLogDao(): CrashLogDao

    companion object {
        @Volatile private var instance: CrashLogDatabaseImpl? = null

        fun getInstance(context: Context): CrashLogDatabaseImpl =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    CrashLogDatabaseImpl::class.java,
                    "crash_logs.db"
                ).fallbackToDestructiveMigration()
                .build().also { instance = it }
            }
    }
}

/**
 * Wrapper class for crash log database operations.
 * Handles serialization and automatic cleanup of old logs.
 */
class CrashLogDatabase(private val context: Context) {
    private val database = CrashLogDatabaseImpl.getInstance(context)
    private val dao = database.crashLogDao()
    private val gson = Gson()

    /**
     * Store a crash log. Automatically cleans up if more than 50 crashes exist.
     */
    suspend fun addCrash(crash: CrashLog) {
        val entity = CrashLogEntity(
            id = crash.id,
            timestamp = crash.timestamp,
            appVersion = crash.appVersion,
            crashJsonData = gson.toJson(crash)
        )
        dao.insertCrash(entity)

        // Clean up if we exceed 50 crashes
        if (dao.getCrashCount() > 50) {
            dao.deleteCrashesOlderThan50()
        }
    }

    /**
     * Get the 5 most recent crashes.
     */
    suspend fun getRecentCrashes(limit: Int = 5): List<CrashLog> {
        return dao.getRecentCrashes(limit).mapNotNull { entity ->
            try {
                gson.fromJson(entity.crashJsonData, CrashLog::class.java)
            } catch (e: Exception) {
                null  // Skip malformed entries
            }
        }
    }

    /**
     * Get a specific crash by ID.
     */
    suspend fun getCrashById(id: String): CrashLog? {
        val entity = dao.getCrashById(id) ?: return null
        return try {
            gson.fromJson(entity.crashJsonData, CrashLog::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get all crashes from a specific app version.
     */
    suspend fun getCrashesByVersion(version: String): List<CrashLog> {
        return dao.getCrashesByVersion(version).mapNotNull { entity ->
            try {
                gson.fromJson(entity.crashJsonData, CrashLog::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Get count of stored crashes.
     */
    suspend fun getCrashCount(): Int {
        return dao.getCrashCount()
    }
}
