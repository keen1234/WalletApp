package com.example.walletapp.ui.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.walletapp.ui.screens.AddTransactionScreen
import com.example.walletapp.ui.screens.BudgetsScreen
import com.example.walletapp.ui.screens.DashboardScreen
import com.example.walletapp.ui.screens.NavRoutes
import com.example.walletapp.ui.screens.StatisticsScreen
import com.example.walletapp.viewmodel.MainViewModel


@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val vm: MainViewModel = viewModel()


    NavHost(navController = navController, startDestination = NavRoutes.DASHBOARD) {
        composable(NavRoutes.DASHBOARD) {
            DashboardScreen(
                viewModel = vm,
                onAllBudgetsClicked = { navController.navigate(NavRoutes.BUDGETS) },
                onStatisticsClicked = { navController.navigate(NavRoutes.STATISTICS) }
            )
        }

        composable(NavRoutes.BUDGETS) {
            BudgetsScreen(
                viewModel = vm,
                onBack = {
                navController.popBackStack()
            })
        }

        composable(NavRoutes.ADD_TRANSACTION) {
            AddTransactionScreen(
                viewModel = vm,
                onSaved = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.STATISTICS) {
            StatisticsScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}