package com.example.walletapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walletapp.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StatisticsScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val transactions by viewModel.transactions.collectAsState()
    val expenseByCategory = transactions.filter { it.type == "expense" }
        .groupBy { it.category }
        .mapValues { it.value.sumOf { tx -> tx.amount } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            )
        },
        backgroundColor = Color(0xFF1A1A1A)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                DateSelector()
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                val expenseByCategory = transactions.filter { it.type == "expense" }
                    .groupBy { it.category }
                    .mapValues { it.value.sumOf { tx -> tx.amount } }
                val totalExpense = expenseByCategory.values.sum()
                val categoryColors = remember {
                    listOf(
                        Color.Green,
                        Color.Cyan,
                        Color.Magenta,
                        Color.Yellow,
                        Color.Red
                    )
                }
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    DonutChart(expenseByCategory, categoryColors, totalExpense)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Expense", color = Color.Gray)
                        Text(
                            NumberFormat.getCurrencyInstance(Locale("en", "PH")).format(totalExpense),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(expenseByCategory.toList()) { (category, amount) ->
                CategoryProgress(category = category, amount = amount, total = expenseByCategory.values.sum())
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CategoryProgress(category: String, amount: Double, total: Double) {
    val progress = (amount / total).toFloat()
    val color = when (category) {
        "Food" -> Color.Blue
        "Entertainment" -> Color.Red
        "Health" -> Color(0xFF8A2BE2)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color(0xFF2C2C2C)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(category, color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    NumberFormat.getCurrencyInstance(Locale("en", "PH")).format(amount),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = color
            )
        }
    }
}
