package com.isaacshub.app.banking.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isaacshub.app.banking.data.BankingRepository
import com.isaacshub.app.banking.domain.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionListViewModel(
    private val repository: BankingRepository,
    private val accountId: String
) : ViewModel() {

    val transactions: Flow<List<Transaction>> = repository.observeTransactionsByAccount(accountId)

    class Factory(
        private val repository: BankingRepository,
        private val accountId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TransactionListViewModel(repository, accountId) as T
        }
    }
}
