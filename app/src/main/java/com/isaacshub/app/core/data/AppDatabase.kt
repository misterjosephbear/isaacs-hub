package com.isaacshub.app.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.isaacshub.app.banking.data.BankAccountDao
import com.isaacshub.app.banking.data.BankAccountEntity
import com.isaacshub.app.banking.data.BankConnectionDao
import com.isaacshub.app.banking.data.BankConnectionEntity
import com.isaacshub.app.banking.data.TransactionDao
import com.isaacshub.app.banking.data.TransactionEntity
import com.isaacshub.app.featurefunnel.data.FeaturePromptDao
import com.isaacshub.app.featurefunnel.data.FeaturePromptEntity
import com.isaacshub.app.sleep.data.SleepSessionDao
import com.isaacshub.app.sleep.data.SleepSessionEntity

/**
 * Consolidated database for general app features.
 * Combines Sleep, Banking, and FeatureFunnel databases.
 */
@Database(
    entities = [
        SleepSessionEntity::class,
        BankConnectionEntity::class,
        BankAccountEntity::class,
        TransactionEntity::class,
        FeaturePromptEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sleepSessionDao(): SleepSessionDao
    abstract fun bankConnectionDao(): BankConnectionDao
    abstract fun bankAccountDao(): BankAccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun featurePromptDao(): FeaturePromptDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app.db"
                )
                    .fallbackToDestructiveMigration() // Allow destructive migration for development
                    .build().also { instance = it }
            }
    }
}
