package com.example.walletapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walletapp.viewmodel.MainViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen(viewModel: MainViewModel, onAllBudgetsClicked: () -> Unit, onStatisticsClicked: () -> Unit) {
    val transactions by viewModel.transactions.collectAsState()
    val totalBalance = transactions.fold(0.0) { acc, t -> if (t.type == "income") acc + t.amount else acc - t.amount }
    val totalSpent = transactions.filter { it.type == "expense" }.sumOf { it.amount }
    val budget = 14500.00
    val remainingBudget = budget - totalSpent
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { SelectAccountSheet() },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = Color(0xFF1A1A1A),
        sheetContentColor = Color.White
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF1A1A1A)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    DateSelector()
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    TotalBalanceCard(totalBalance) {
                        coroutineScope.launch {
                            sheetState.show()
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    BudgetCard(budget, remainingBudget, totalSpent, onAllBudgetsClicked)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    CategoriesCard(transactions, onStatisticsClicked)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun DateSelector() {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val currentDate = dateFormat.format(calendar.time)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month", tint = Color.White)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = currentDate.uppercase(Locale.getDefault()),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month", tint = Color.White)
        }
    }
}

@Composable
fun TotalBalanceCard(balance: Double, onMoreClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onMoreClicked),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFF2C2C2C)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total Balance", color = Color.Gray)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "More", tint = Color.White)
            }
            Text(
                text = NumberFormat.getCurrencyInstance(Locale("en", "PH")).format(balance),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SelectAccountSheet() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Select Account",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
        AccountItem("Total Balance", "$26,000.40", Icons.Default.AccountBalance)
        AccountItem("Credit Card", "$10,000", Icons.Default.CreditCard)
        AccountItem("Debit Card", "$13,000.40", Icons.Default.Payment)
        AccountItem("Cash", "$3,000", Icons.Default.Money)
    }
}

@Composable
fun AccountItem(name: String, balance: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = name, tint = Color.White)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(name, fontWeight = FontWeight.Bold)
            Text(balance, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun BudgetCard(budget: Double, remaining: Double, spent: Double, onAllBudgetsClicked: () -> Unit) {
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
                Text("Budget", color = Color.Gray)
                Text("All Budgets", color = Color(0xFF8A2BE2), modifier = Modifier.clickable(onClick = onAllBudgetsClicked))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${NumberFormat.getCurrencyInstance(Locale("en", "PH")).format(remaining)} left",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "-${NumberFormat.getCurrencyInstance(Locale("en", "PH")).format(spent)} spent this month",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = (spent / budget).toFloat(),
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF8A2BE2),
                backgroundColor = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BudgetCategory(color = Color.Green, category = "Entertainment", amount = 3430.0)
                BudgetCategory(color = Color.Red, category = "Food", amount = 430.0)
                BudgetCategory(color = Color.Blue, category = "Other", amount = 0.0)
            }
        }
    }
}

@Composable
fun BudgetCategory(color: Color, category: String, amount: Double) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(category, color = Color.White, fontSize = 12.sp)
            Text(
                "${NumberFormat.getCurrencyInstance(Locale("en", "PH")).format(amount)} spent",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun CategoriesCard(transactions: List<com.example.walletapp.model.Transaction>, onStatisticsClicked: () -> Unit) {
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
                Text("Categories", color = Color.Gray)
                Text("Statistics", color = Color(0xFF8A2BE2), modifier = Modifier.clickable(onClick = onStatisticsClicked))
            }
            Spacer(modifier = Modifier.height(16.dp))
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
        }
    }
}

@Composable
fun DonutChart(
    data: Map<String, Double>,
    colors: List<Color>,
    total: Double
) {
    var startAngle = -90f
    Canvas(modifier = Modifier.size(180.dp)) {
        data.onEachIndexed { index, entry ->
            val sweepAngle = (entry.value / total * 360).toFloat()
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 30f, cap = StrokeCap.Butt)
            )
            startAngle += sweepAngle
        }
    }
}