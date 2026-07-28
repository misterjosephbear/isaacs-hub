package com.isaacshub.app.banking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_account_selections")
data class BudgetAccountSelectionEntity(
    @PrimaryKey val accountId: String,  // References BankAccountEntity.id (no FK - different DB)
    val isIncluded: Boolean              // true if this account counts toward budget
)
