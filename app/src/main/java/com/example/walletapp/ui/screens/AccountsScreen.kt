package com.example.walletapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walletapp.model.Account
import com.example.walletapp.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AccountsScreen(viewModel: MainViewModel) {
    val accounts by viewModel.accounts.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddAccountDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, type, balance ->
                viewModel.addAccount(Account(name = name, type = type, balance = balance))
                showAddDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Accounts") },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Account")
                    }
                },
                backgroundColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            )
        },
        backgroundColor = Color(0xFF1A1A1A)
    ) { padding ->
        if (accounts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No accounts yet", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(accounts, key = { it.id }) { account ->
                    AccountRow(account = account, onDelete = { viewModel.deleteAccount(account) })
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun AccountRow(account: Account, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CircleShape,
        backgroundColor = Color(0xFF2C2C2C)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(account.name, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(account.type, color = Color.Gray, fontSize = 12.sp)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                val amount = NumberFormat.getCurrencyInstance(Locale("en", "US")).format(account.balance)
                Text(amount, color = Color.White, fontWeight = FontWeight.Bold)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
private fun AddAccountDialog(onDismiss: () -> Unit, onAdd: (String, String, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Credit Card") }
    var balanceText by remember { mutableStateOf("0.00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Account") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Account Name") },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White)
                )
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Account Type") },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White)
                )
                OutlinedTextField(
                    value = balanceText,
                    onValueChange = { balanceText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Starting Balance") },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val balance = balanceText.toDoubleOrNull() ?: 0.0
                if (name.isNotBlank()) onAdd(name.trim(), type.trim(), balance)
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        backgroundColor = Color(0xFF2C2C2C),
        contentColor = Color.White
    )
}

