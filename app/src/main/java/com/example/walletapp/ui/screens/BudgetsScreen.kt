package com.example.walletapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walletapp.model.Budget
import com.example.walletapp.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

@Composable
fun BudgetsScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    var showCreateBudgetScreen by remember { mutableStateOf(false) }
    var showEditBudgetScreen by remember { mutableStateOf(false) }
    var editingBudget by remember { mutableStateOf<Budget?>(null) }

    val budgets by viewModel.budgets.collectAsState()

    if (showCreateBudgetScreen) {
        CreateBudgetScreen(
            onDismiss = { showCreateBudgetScreen = false },
            onCreateBudget = {
                showCreateBudgetScreen = false
                // TODO: Add budget logic
            }
        )
    }

    editingBudget?.let { budget ->
        if (showEditBudgetScreen) {
            EditBudgetScreen(
                budget = budget,
                onDismiss = { showEditBudgetScreen = false },
                onSaveBudget = { updatedBudget ->
                    viewModel.updateBudget(updatedBudget)
                    showEditBudgetScreen = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Budgets") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showCreateBudgetScreen = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Budget")
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
            items(budgets) { budget ->
                BudgetSummaryCard(
                    budget = budget,
                    onEdit = {
                        editingBudget = it
                        showEditBudgetScreen = true
                    },
                    onDelete = { viewModel.deleteBudget(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun BudgetSummaryCard(
    budget: Budget,
    onEdit: (Budget) -> Unit,
    onDelete: (Budget) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFF2C2C2C)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(budget.title, color = budget.titleColor, fontWeight = FontWeight.Bold)
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options", tint = Color.Gray)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(Color(0xFF2C2C2C))
                    ) {
                        DropdownMenuItem(onClick = {
                            showMenu = false
                            onEdit(budget)
                        }) {
                            Text("Edit Budget", color = Color.White)
                        }
                        DropdownMenuItem(onClick = {
                            showMenu = false
                            onDelete(budget)
                        }) {
                            Text("Delete Budget", color = Color.Red)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    NumberFormat.getCurrencyInstance(Locale("en", "PH")).format(budget.amount),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${budget.percentage}%",
                    color = if (budget.isOverspending) Color.Red else Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = budget.progress,
                modifier = Modifier.fillMaxWidth(),
                color = budget.progressColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "-$${NumberFormat.getNumberInstance(Locale.US).format(budget.spent)} spent",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    if (budget.isOverspending) "$${NumberFormat.getNumberInstance(Locale.US).format(budget.left)} overspending"
                    else "$${NumberFormat.getNumberInstance(Locale.US).format(budget.left)} left",
                    color = if (budget.isOverspending) Color.Red else Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}
