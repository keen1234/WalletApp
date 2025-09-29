package com.example.walletapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.walletapp.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale


@Composable
fun AnalyticsScreen(viewModel: MainViewModel) {
    val txs = viewModel.transactions.collectAsState()
    val total = txs.value.fold(0.0) { acc, t ->
        if (t.type == "income") acc + t.amount else acc - t.amount
    }

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Balance", style = MaterialTheme.typography.subtitle1)
                Text(text = NumberFormat.getCurrencyInstance(philippinePeso).format(total), style = MaterialTheme.typography.h5)
            }
        }
    }
}
