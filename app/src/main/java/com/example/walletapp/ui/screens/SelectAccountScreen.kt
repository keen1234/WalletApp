package com.example.walletapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.walletapp.model.Account
import com.example.walletapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAccountScreen(
    viewModel: MainViewModel,
    onAccountSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val accounts by viewModel.accounts.collectAsState()
    var showAddAccountScreen by remember { mutableStateOf(false) }

    if (showAddAccountScreen) {
        AddAccountScreen(
            onAddAccount = { accountName ->
                viewModel.addAccount(Account(name = accountName, type = "Account", balance = 0.0))
                showAddAccountScreen = false
            },
            onDismiss = { showAddAccountScreen = false }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Select Account") },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1A1A1A),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            },
            containerColor = Color(0xFF1A1A1A)
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                accounts.forEach { account ->
                    AccountItem(
                        accountName = account.name,
                        icon = Icons.Default.CreditCard,
                        onClick = {
                            onAccountSelected(account.name)
                            onDismiss()
                        },
                        onDelete = {
                            viewModel.deleteAccount(account)
                        }
                    )
                }
                AccountItem(
                    accountName = "Add New Account",
                    icon = Icons.Default.Add,
                    onClick = {
                        showAddAccountScreen = true
                    },
                    onDelete = null
                )
            }
        }
    }
}

@Composable
fun AccountItem(
    accountName: String,
    icon: ImageVector,
    onClick: () -> Unit,
    onDelete: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = accountName, tint = Color.White)
            Spacer(modifier = Modifier.width(16.dp))
            Text(accountName, color = Color.White, fontSize = 16.sp)
        }
        if (onDelete != null) {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
            }
        }
    }
}
