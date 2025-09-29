package com.example.walletapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.walletapp.model.Budget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBudgetScreen(
    budget: Budget,
    onDismiss: () -> Unit,
    onSaveBudget: (Budget) -> Unit
) {
    var budgetName by remember { mutableStateOf(budget.title) }
    var monthlyBudget by remember { mutableStateOf(String.format("$%.2f", budget.amount)) }
    var budgetColor by remember { mutableStateOf(budget.titleColor) }

    Dialog(onDismissRequest = onDismiss) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Budget") },
                    actions = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1A1A1A),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            },
            containerColor = Color(0xFF1A1A1A),
            bottomBar = {
                Button(
                    onClick = {
                        val updatedBudget = budget.copy(
                            title = budgetName,
                            amount = monthlyBudget.removePrefix("$").replace(",", "").toDouble(),
                            titleColor = budgetColor
                        )
                        onSaveBudget(updatedBudget)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F52FF))
                ) {
                    Text("Save Budget", color = Color.White)
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Monthly Budget", color = Color.Gray, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = monthlyBudget,
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))

                Divider(color = Color.Gray)

                // Budget Name
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Budget Name", color = Color.White, fontSize = 16.sp)
                    Text(budgetName, color = Color.Gray, fontSize = 16.sp)
                }
                Divider(color = Color.Gray)

                // Budget Color
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Budget Color", color = Color.White, fontSize = 16.sp)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(budgetColor, CircleShape)
                    )
                }
                Divider(color = Color.Gray)

                // Accounts
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Accounts", color = Color.White, fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("All Accounts", color = Color.Gray, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.AccountBalance, contentDescription = "Accounts", tint = Color.White)
                    }
                }
                Divider(color = Color.Gray)

                // Categories
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Categories", color = Color.White, fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("19 Categories", color = Color.Gray, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Category, contentDescription = "Categories", tint = Color.White)
                    }
                }
                Divider(color = Color.Gray)
            }
        }
    }
}

