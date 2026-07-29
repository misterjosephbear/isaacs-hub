package com.isaacshub.app.banking.domain

/**
 * Represents a single transaction from a bank account.
 */
data class Transaction(
    val id: String,
    val accountId: String,
    val amount: Double,
    val date: Long,
    val name: String,
    val merchantName: String? = null,
    val category: List<String>? = null,
    val pending: Boolean = false,
    val paymentChannel: String? = null,
    val lastUpdated: Long
)
