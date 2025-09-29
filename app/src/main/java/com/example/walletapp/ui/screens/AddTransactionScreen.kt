package com.example.walletapp.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Train
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walletapp.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddTransactionScreen(viewModel: MainViewModel, onSaved: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("Expense", "Income", "Transfer")


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Record") },
                navigationIcon = {
                    IconButton(onClick = onSaved) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF1A1A1A)
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Color(0xFF7F52FF)
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        selectedContentColor = Color(0xFF7F52FF),
                        unselectedContentColor = Color.Gray
                    )
                }
            }


            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> ExpenseTab(viewModel = viewModel, onSaved = onSaved)
                    1 -> IncomeTab(viewModel = viewModel, onSaved = onSaved)
                    2 -> TransferTab(viewModel = viewModel, onSaved = onSaved)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTab(viewModel: MainViewModel, onSaved: () -> Unit) {
    var amountText by remember { mutableStateOf("0") }
    var account by remember { mutableStateOf("Credit Card") }
    var category by remember { mutableStateOf("Transport") }
    var subcategory by remember { mutableStateOf("Plane") }
    var date by remember { mutableStateOf(Date()) }
    var notes by remember { mutableStateOf("From airport to city center") }
    val showDatePicker = remember { mutableStateOf(false) }
    var showAmountInput by remember { mutableStateOf(false) }
    var showSelectAccount by remember { mutableStateOf(false) }

    if (showSelectAccount) {
        SelectAccountScreen(
            viewModel = viewModel,
            onAccountSelected = {
                account = it
                showSelectAccount = false
            },
            onDismiss = { showSelectAccount = false }
        )
    }

    if (showAmountInput) {
        AmountInput(
            amount = amountText,
            onAmountChange = { amountText = it },
            onSave = { showAmountInput = false },
            recordType = "expense"
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Expense", color = Color.Gray)
            Text(
                "-$${amountText}",
                color = Color.Red,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { showAmountInput = true }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Account
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { showSelectAccount = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Account", color = Color.Gray, fontSize = 12.sp)
                    Text(account, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Default.CreditCard, contentDescription = "Account", tint = Color.White)
            }
            HorizontalDivider(color = Color.Gray)

            // Category
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Category", color = Color.Gray, fontSize = 12.sp)
                    Text(category, color = Color.Green, fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Default.Train, contentDescription = "Category", tint = Color.Green)
            }
            HorizontalDivider(color = Color.Gray)

            // Subcategory
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Subcategory",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Chip("Taxi", subcategory == "Taxi") { subcategory = "Taxi" }
                    Chip("Rent", subcategory == "Rent") { subcategory = "Rent" }
                    Chip("Plane", subcategory == "Plane") { subcategory = "Plane" }
                    IconButton(onClick = { /* TODO: Add subcategory */ }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Subcategory",
                            tint = Color.White
                        )
                    }
                }
            }
            HorizontalDivider(color = Color.Gray)

            // Date & Time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Date & Time", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(date),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { showDatePicker.value = true }) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Select date",
                        tint = Color.White
                    )
                }
            }
            HorizontalDivider(color = Color.Gray)

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = textFieldColors()
            )

            if (showDatePicker.value) {
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.time)
                DatePickerDialog(
                    onDismissRequest = { showDatePicker.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            date =
                                Date(datePickerState.selectedDateMillis ?: System.currentTimeMillis())
                            showDatePicker.value = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker.value = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 0.0
                    viewModel.addTransaction(notes, amt, "expense", category, date.time)
                    onSaved()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F52FF))
            ) {
                Text("Add Record", color = Color.White)
            }
        }
    }
}

@Composable
fun Chip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Green.copy(alpha = 0.2f) else Color.DarkGray,
            contentColor = if (isSelected) Color.Green else Color.White
        )
    ) {
        Text(label)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeTab(viewModel: MainViewModel, onSaved: () -> Unit) {
    var amountText by remember { mutableStateOf("0") }
    var account by remember { mutableStateOf("Credit Card") }
    var category by remember { mutableStateOf("Income") }
    var subcategory by remember { mutableStateOf("Rent") }
    var date by remember { mutableStateOf(Date()) }
    var notes by remember { mutableStateOf("Brooklyn Apartment Rent") }
    val showDatePicker = remember { mutableStateOf(false) }
    var showAmountInput by remember { mutableStateOf(false) }
    var showSelectAccount by remember { mutableStateOf(false) }

    if (showSelectAccount) {
        SelectAccountScreen(
            viewModel = viewModel,
            onAccountSelected = {
                account = it
                showSelectAccount = false
            },
            onDismiss = { showSelectAccount = false }
        )
    }

    if (showAmountInput) {
        AmountInput(
            amount = amountText,
            onAmountChange = { amountText = it },
            onSave = { showAmountInput = false },
            recordType = "income"
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Income", color = Color.Gray)
            Text(
                "$${amountText}",
                color = Color.Green,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { showAmountInput = true }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Account
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { showSelectAccount = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Account", color = Color.Gray, fontSize = 12.sp)
                    Text(account, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Default.CreditCard, contentDescription = "Account", tint = Color.White)
            }
            HorizontalDivider(color = Color.Gray)

            // Category
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Category", color = Color.Gray, fontSize = 12.sp)
                    Text(category, color = Color.Green, fontWeight = FontWeight.Bold)
                }
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = "Category",
                    tint = Color.Green
                )
            }
            HorizontalDivider(color = Color.Gray)

            // Subcategory
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Subcategory",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Chip("Salary", subcategory == "Salary") { subcategory = "Salary" }
                    Chip("Interest", subcategory == "Interest") { subcategory = "Interest" }
                    Chip("Rent", subcategory == "Rent") { subcategory = "Rent" }
                    IconButton(onClick = { /* TODO: Add subcategory */ }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Subcategory",
                            tint = Color.White
                        )
                    }
                }
            }
            HorizontalDivider(color = Color.Gray)

            // Date & Time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Date & Time", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(date),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { showDatePicker.value = true }) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Select date",
                        tint = Color.White
                    )
                }
            }
            HorizontalDivider(color = Color.Gray)

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = textFieldColors()
            )

            if (showDatePicker.value) {
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.time)
                DatePickerDialog(
                    onDismissRequest = { showDatePicker.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            date =
                                Date(datePickerState.selectedDateMillis ?: System.currentTimeMillis())
                            showDatePicker.value = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker.value = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 0.0
                    viewModel.addTransaction(notes, amt, "income", category, date.time)
                    onSaved()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F52FF))
            ) {
                Text("Add Record", color = Color.White)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferTab(viewModel: MainViewModel, onSaved: () -> Unit) {
    var amountText by remember { mutableStateOf("0") }
    var fromAccount by remember { mutableStateOf("Cash") }
    var toAccount by remember { mutableStateOf("Credit Card") }
    var date by remember { mutableStateOf(Date()) }
    var notes by remember { mutableStateOf("Brooklyn Apartment Rent") }
    val showDatePicker = remember { mutableStateOf(false) }
    var showAmountInput by remember { mutableStateOf(false) }
    var showSelectFromAccount by remember { mutableStateOf(false) }
    var showSelectToAccount by remember { mutableStateOf(false) }

    if (showSelectFromAccount) {
        SelectAccountScreen(
            viewModel = viewModel,
            onAccountSelected = {
                fromAccount = it
                showSelectFromAccount = false
            },
            onDismiss = { showSelectFromAccount = false }
        )
    }

    if (showSelectToAccount) {
        SelectAccountScreen(
            viewModel = viewModel,
            onAccountSelected = {
                toAccount = it
                showSelectToAccount = false
            },
            onDismiss = { showSelectToAccount = false }
        )
    }

    if (showAmountInput) {
        AmountInput(
            amount = amountText,
            onAmountChange = { amountText = it },
            onSave = { showAmountInput = false },
            recordType = "transfer"
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Transfer", color = Color.Gray)
            Text(
                "$${amountText}",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { showAmountInput = true }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // From Account
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { showSelectFromAccount = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("From Account", color = Color.Gray, fontSize = 12.sp)
                    Text(fromAccount, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = "From Account",
                    tint = Color.White
                )
            }
            HorizontalDivider(color = Color.Gray)

            // To Account
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { showSelectToAccount = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("To Account", color = Color.Gray, fontSize = 12.sp)
                    Text(toAccount, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Default.CreditCard, contentDescription = "To Account", tint = Color.White)
            }
            HorizontalDivider(color = Color.Gray)

            // Date & Time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Date & Time", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(date),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { showDatePicker.value = true }) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Select date",
                        tint = Color.White
                    )
                }
            }
            HorizontalDivider(color = Color.Gray)

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = textFieldColors()
            )

            if (showDatePicker.value) {
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.time)
                DatePickerDialog(
                    onDismissRequest = { showDatePicker.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            date =
                                Date(datePickerState.selectedDateMillis ?: System.currentTimeMillis())
                            showDatePicker.value = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker.value = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 0.0
                    viewModel.addTransaction(
                        "Transfer from $fromAccount to $toAccount",
                        amt,
                        "transfer",
                        "Transfer",
                        date.time
                    )
                    onSaved()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F52FF))
            ) {
                Text("Add Record", color = Color.White)
            }
        }
    }
}

@Composable
private fun textFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.Gray
    )
}
