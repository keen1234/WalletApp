package com.example.walletapp.data


import androidx.room.*
import com.example.walletapp.model.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Transaction>>


    @Insert
    suspend fun insert(tx: Transaction): Long


    @Delete
    suspend fun delete(tx: Transaction)
}
