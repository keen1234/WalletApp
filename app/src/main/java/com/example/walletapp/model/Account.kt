package com.example.walletapp.model

import java.util.UUID

data class Account(
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var type: String,
    var balance: Double
)

