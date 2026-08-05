package com.isaacshub.app.banking.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        BudgetCategoryEntity::class,
        BudgetAccountSelectionEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class BankingDatabase : RoomDatabase() {

    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile private var instance: BankingDatabase? = null

        fun getInstance(context: Context): BankingDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BankingDatabase::class.java,
                    "budget.db" // Renamed to avoid conflicts with old banking.db
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Insert default budget categories
                        db.execSQL("""
                            INSERT INTO budget_categories VALUES
                            ('crucial', 'Crucial Budget', 2000.0, 0, '#FF6200EE', '🏠'),
                            ('utility', 'Utility Budget', 1500.0, 1, '#FF03DAC5', '🔧'),
                            ('convenience', 'Convenience Budget', 1000.0, 2, '#FF018786', '🎁'),
                            ('frivolous', 'Frivolous Budget', 500.0, 3, '#FFB00020', '💎')
                        """)
                    }
                })
                .build()
                .also { instance = it }
            }
    }
}
