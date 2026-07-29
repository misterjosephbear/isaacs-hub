package com.isaacshub.app.banking.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.isaacshub.app.banking.domain.Transaction

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = BankAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("accountId"), Index("date")]
)
data class TransactionEntity(
    @PrimaryKey val id: String,
    val accountId: String,
    val amount: Double,
    val date: Long,
    val name: String,
    val merchantName: String?,
    val category: String?, // Stored as comma-separated string
    val pending: Boolean,
    val paymentChannel: String?,
    val lastUpdated: Long
) {
    companion object {
        fun fromDomain(transaction: Transaction): TransactionEntity {
            return TransactionEntity(
                id = transaction.id,
                accountId = transaction.accountId,
                amount = transaction.amount,
                date = transaction.date,
                name = transaction.name,
                merchantName = transaction.merchantName,
                category = transaction.category?.joinToString(","),
                pending = transaction.pending,
                paymentChannel = transaction.paymentChannel,
                lastUpdated = transaction.lastUpdated
            )
        }
    }

    fun toDomain(): Transaction {
        return Transaction(
            id = id,
            accountId = accountId,
            amount = amount,
            date = date,
            name = name,
            merchantName = merchantName,
            category = category?.split(",")?.filter { it.isNotBlank() },
            pending = pending,
            paymentChannel = paymentChannel,
            lastUpdated = lastUpdated
        )
    }
}
