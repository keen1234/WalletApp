package com.example.walletapp.repository


import com.example.walletapp.data.TransactionDao
import com.example.walletapp.model.Transaction
import kotlinx.coroutines.flow.Flow


class TransactionRepository(private val dao: TransactionDao) {
    fun allTransactions(): Flow<List<Transaction>> = dao.getAll()


    suspend fun addTransaction(tx: Transaction) = dao.insert(tx)


    suspend fun deleteTransaction(tx: Transaction) = dao.delete(tx)
}