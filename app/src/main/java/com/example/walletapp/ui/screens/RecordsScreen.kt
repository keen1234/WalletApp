package com.example.walletapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walletapp.model.Transaction
import com.example.walletapp.viewmodel.MainViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onAddRecord: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsState()
    val groupedTransactions = transactions.groupBy {
        val cal = Calendar.getInstance()
        cal.timeInMillis = it.timestamp
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(cal.time)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Records") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onAddRecord) {
                        Icon(Icons.Default.Add, contentDescription = "Add Record")
                    }
                },
                backgroundColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            )
        },
        backgroundColor = Color(0xFF1A1A1A)
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Previous", tint = Color.White)
                }
                Text("This Year", color = Color.White)
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "Next", tint = Color.White)
                }
            }
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Search Record") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    leadingIconColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color.White,
                    placeholderColor = Color.Gray,
                    disabledPlaceholderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                groupedTransactions.forEach { (date, transactionsOnDate) ->
                    item {
                        Text(
                            text = date,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(transactionsOnDate, key = { it.id }) { transaction ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToStart) {
                                    viewModel.deleteTransaction(transaction)
                                    true
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(DismissDirection.EndToStart),
                            background = {
                                val color = when (dismissState.dismissDirection) {
                                    DismissDirection.EndToStart -> Color.Red
                                    else -> Color.Transparent
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color, shape = RoundedCornerShape(16.dp))
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }
                            }
                        ) {
                            TransactionListItem(transaction = transaction)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionListItem(transaction: Transaction) {
    val icon = when (transaction.category) {
        "Shopping" -> Icons.Default.ShoppingCart
        "Salary" -> Icons.Default.AccountBalanceWallet
        "Vacation" -> Icons.Default.Landscape
        else -> Icons.Default.Receipt
    }
    val iconColor = when (transaction.category) {
        "Shopping" -> Color.Yellow
        "Salary" -> Color.Green
        "Vacation" -> Color.Red
        else -> Color.White
    }
    val amountColor = if (transaction.type == "income") Color.Green else Color.Red

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFF2C2C2C)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = transaction.category, tint = iconColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(transaction.title, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(transaction.category, color = Color.Gray, fontSize = 12.sp)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = (if (transaction.type == "income") "+" else "-") +
                            NumberFormat.getCurrencyInstance(Locale("en", "US")).format(transaction.amount),
                    color = amountColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(transaction.timestamp)),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}
