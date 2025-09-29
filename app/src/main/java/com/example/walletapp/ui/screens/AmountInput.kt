package com.example.walletapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AmountInput(
    amount: String,
    onAmountChange: (String) -> Unit,
    onSave: () -> Unit,
    recordType: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when (recordType) {
                "expense" -> "-$${amount}"
                "income" -> "$${amount}"
                else -> "$${amount}"
            },
            color = when (recordType) {
                "expense" -> Color.Red
                "income" -> Color.Green
                else -> Color.White
            },
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        val numberButton: @Composable (String) -> Unit = { number ->
            Button(
                onClick = { onAmountChange(amount + number) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(number, fontSize = 24.sp, color = Color.White)
            }
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                numberButton("1")
                numberButton("2")
                numberButton("3")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                numberButton("4")
                numberButton("5")
                numberButton("6")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                numberButton("7")
                numberButton("8")
                numberButton("9")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        if (!amount.contains(".")) {
                            onAmountChange("$amount.")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(".", fontSize = 24.sp, color = Color.White)
                }
                numberButton("0")
                Button(
                    onClick = {
                        if (amount.isNotEmpty()) {
                            onAmountChange(amount.dropLast(1))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        Icons.Default.Backspace,
                        contentDescription = "Backspace",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F52FF))
        ) {
            Text("Save", color = Color.White)
        }
    }
}

