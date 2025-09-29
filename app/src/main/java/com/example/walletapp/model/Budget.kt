package com.example.walletapp.model

import androidx.compose.ui.graphics.Color
import java.util.UUID

data class Budget(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    var amount: Double,
    val spent: Double,
    val progress: Float,
    val progressColor: Color,
    val percentage: Int,
    val left: Double,
    val isOverspending: Boolean = false,
    val titleColor: Color = Color.White
)

