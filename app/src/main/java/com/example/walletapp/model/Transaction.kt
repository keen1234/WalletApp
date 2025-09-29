package com.example.walletapp.model


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val type: String, // "income" or "expense"
    val category: String,
    val timestamp: Long
)