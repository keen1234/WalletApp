package com.example.walletapp.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.walletapp.data.AppDatabase
import com.example.walletapp.model.Account
import com.example.walletapp.model.Budget
import com.example.walletapp.model.Transaction
import com.example.walletapp.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val repo = TransactionRepository(db.transactionDao())


    val transactions = repo.allTransactions()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _budgets = MutableStateFlow<List<Budget>>(emptyList())
    val budgets: StateFlow<List<Budget>> = _budgets.asStateFlow()

    private val _accounts = MutableStateFlow<List<Account>>(
        listOf(
            Account(name = "Credit Card", type = "Credit Card", balance = 1500.0),
            Account(name = "Debit Card", type = "Debit Card", balance = 2500.0),
            Account(name = "Cash", type = "Cash", balance = 500.0)
        )
    )
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()


    fun addTransaction(title: String, amount: Double, type: String, category: String, timestamp: Long) {
        val tx = Transaction(
            title = title,
            amount = amount,
            type = type,
            category = category,
            timestamp = timestamp
        )
        viewModelScope.launch {
            repo.addTransaction(tx)
        }
    }


    fun deleteTransaction(tx: Transaction) {
        viewModelScope.launch {
            repo.deleteTransaction(tx)
        }
    }

    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            val currentBudgets = _budgets.value.toMutableList()
            currentBudgets.add(budget)
            _budgets.value = currentBudgets
        }
    }

    fun updateBudget(budget: Budget) {
        viewModelScope.launch {
            val currentBudgets = _budgets.value.toMutableList()
            val index = currentBudgets.indexOfFirst { it.id == budget.id }
            if (index != -1) {
                currentBudgets[index] = budget
                _budgets.value = currentBudgets
            }
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            val currentBudgets = _budgets.value.toMutableList()
            currentBudgets.remove(budget)
            _budgets.value = currentBudgets
        }
    }

    fun addAccount(account: Account) {
        viewModelScope.launch {
            val currentAccounts = _accounts.value.toMutableList()
            currentAccounts.add(account)
            _accounts.value = currentAccounts
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch {
            val currentAccounts = _accounts.value.toMutableList()
            val index = currentAccounts.indexOfFirst { it.id == account.id }
            if (index != -1) {
                currentAccounts[index] = account
                _accounts.value = currentAccounts
            }
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            val currentAccounts = _accounts.value.toMutableList()
            currentAccounts.remove(account)
            _accounts.value = currentAccounts
        }
    }
}