package com.isaacshub.app.banking.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY date DESC, pending DESC")
    fun getTransactionsByAccount(accountId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY date DESC, pending DESC LIMIT :limit")
    fun getRecentTransactions(accountId: String, limit: Int): Flow<List<TransactionEntity>>

    @Query("DELETE FROM transactions WHERE accountId = :accountId")
    suspend fun deleteTransactionsByAccount(accountId: String)

    @Query("SELECT COUNT(*) FROM transactions WHERE accountId = :accountId")
    suspend fun getTransactionCount(accountId: String): Int
}
