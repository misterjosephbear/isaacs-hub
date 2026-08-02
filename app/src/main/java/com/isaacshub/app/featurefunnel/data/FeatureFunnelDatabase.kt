package com.isaacshub.app.featurefunnel.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FeaturePromptEntity::class], version = 2, exportSchema = true)
abstract class FeatureFunnelDatabase : RoomDatabase() {

    abstract fun featurePromptDao(): FeaturePromptDao

    companion object {
        @Volatile
        private var instance: FeatureFunnelDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add channelId column with empty default (will need to be set by user)
                db.execSQL("ALTER TABLE feature_prompts ADD COLUMN channelId TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getInstance(context: Context): FeatureFunnelDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FeatureFunnelDatabase::class.java,
                    "feature_funnel.db"
                )
                .addMigrations(MIGRATION_1_2)
                .build().also { instance = it }
            }
    }
}
