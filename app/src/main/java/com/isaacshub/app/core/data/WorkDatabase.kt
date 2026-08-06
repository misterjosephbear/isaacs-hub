package com.isaacshub.app.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.isaacshub.app.routehelper.data.CachedRoadRouteEntity
import com.isaacshub.app.routehelper.data.CandidateAddressEntity
import com.isaacshub.app.routehelper.data.PackageEntity
import com.isaacshub.app.routehelper.data.RouteHelperDao
import com.isaacshub.app.routehelper.data.RouteHelperRouteEntity
import com.isaacshub.app.routehelper.data.RouteSectionEntity
import com.isaacshub.app.routehelper.data.RoutedStopEntity
import com.isaacshub.app.timetracking.data.DeductionDao
import com.isaacshub.app.timetracking.data.DeductionEntity
import com.isaacshub.app.timetracking.data.RouteDao
import com.isaacshub.app.timetracking.data.RouteEntity
import com.isaacshub.app.timetracking.data.RouteScheduleOverrideDao
import com.isaacshub.app.timetracking.data.RouteScheduleOverrideEntity
import com.isaacshub.app.timetracking.data.TimeEntryDao
import com.isaacshub.app.timetracking.data.TimeEntryEntity

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Add isNonRoutable column for non-routable package support (defaults to 0/false)
        db.execSQL("ALTER TABLE `routed_stops` ADD COLUMN `isNonRoutable` INTEGER NOT NULL DEFAULT 0")

        // Change sequenceOrder from INTEGER to REAL (float) to support fractional sequence numbers like 22.5
        // This requires recreating the table since SQLite doesn't support ALTER COLUMN TYPE directly
        db.execSQL("""
            CREATE TABLE `routed_stops_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `routeId` INTEGER NOT NULL,
                `sequenceOrder` REAL NOT NULL,
                `addressLabel` TEXT NOT NULL,
                `note` TEXT,
                `latitude` REAL NOT NULL,
                `longitude` REAL NOT NULL,
                `candidateAddressId` INTEGER,
                `createdAtEpochMillis` INTEGER NOT NULL,
                `recipientLastName` TEXT,
                `expectedPackageCount` INTEGER,
                `isNonRoutable` INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent())

        // Copy data from old table to new table, casting sequenceOrder to REAL
        db.execSQL("""
            INSERT INTO `routed_stops_new`
            SELECT `id`, `routeId`, CAST(`sequenceOrder` AS REAL), `addressLabel`, `note`,
                   `latitude`, `longitude`, `candidateAddressId`, `createdAtEpochMillis`,
                   `recipientLastName`, `expectedPackageCount`, 0
            FROM `routed_stops`
        """.trimIndent())

        // Drop old table and rename new table
        db.execSQL("DROP TABLE `routed_stops`")
        db.execSQL("ALTER TABLE `routed_stops_new` RENAME TO `routed_stops`")
    }
}

/**
 * Consolidated database for work-related features.
 * Combines TimeTracking and RouteHelper databases.
 *
 * Version history:
 * - v1: Initial consolidated database with all entities from both sources
 * - v2: Add isNonRoutable field and change sequenceOrder from INT to FLOAT for non-routable package support
 */
@Database(
    entities = [
        // TimeTracking entities
        TimeEntryEntity::class,
        RouteEntity::class,
        DeductionEntity::class,
        RouteScheduleOverrideEntity::class,
        // RouteHelper entities
        RouteHelperRouteEntity::class,
        CandidateAddressEntity::class,
        RoutedStopEntity::class,
        CachedRoadRouteEntity::class,
        PackageEntity::class,
        RouteSectionEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class WorkDatabase : RoomDatabase() {

    // TimeTracking DAOs
    abstract fun timeEntryDao(): TimeEntryDao
    abstract fun routeDao(): RouteDao
    abstract fun deductionDao(): DeductionDao
    abstract fun routeScheduleOverrideDao(): RouteScheduleOverrideDao

    // RouteHelper DAOs
    abstract fun routeHelperDao(): RouteHelperDao

    companion object {
        @Volatile private var instance: WorkDatabase? = null

        private fun insertDefaultDeductions(db: SupportSQLiteDatabase) {
            // Percentage-based deductions
            db.execSQL("INSERT INTO deductions (name, type, amount) VALUES ('Social Security', 'PERCENT', 6.2)")
            db.execSQL("INSERT INTO deductions (name, type, amount) VALUES ('Medicare', 'PERCENT', 1.45)")
            db.execSQL("INSERT INTO deductions (name, type, amount) VALUES ('Federal Tax: M 00', 'PERCENT', 7.09)")
            db.execSQL("INSERT INTO deductions (name, type, amount) VALUES ('State Income Tax: IN M 00', 'PERCENT', 2.95)")

            // Flat-rate deductions
            db.execSQL("INSERT INTO deductions (name, type, amount) VALUES ('USPS HB Pln After-tax: (Self only) 200', 'FLAT', 84.00)")
            db.execSQL("INSERT INTO deductions (name, type, amount) VALUES ('USPS HB Pln After-tax: (Self + 1) 200', 'FLAT', 34.75)")
            db.execSQL("INSERT INTO deductions (name, type, amount) VALUES ('Allotment', 'FLAT', 1000.00)")
        }

        fun getInstance(context: Context): WorkDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    WorkDatabase::class.java,
                    "work.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate default USPS deductions on fresh install
                            insertDefaultDeductions(db)
                        }
                    })
                    .build().also { instance = it }
            }
    }
}
