package com.example.walletapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walletapp.ui.screens.HomeScreen
import com.example.walletapp.ui.theme.WalletAppTheme
import com.example.walletapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val viewModel: MainViewModel = viewModel()
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
    }
}
