package com.example.walletapp.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Description
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.walletapp.viewmodel.MainViewModel
import androidx.compose.foundation.shape.CircleShape

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavRoutes.ADD_TRANSACTION) },
                backgroundColor = Color(0xFF7F52FF)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add transaction", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar(
                cutoutShape = CircleShape,
                backgroundColor = Color(0xFF2C2C2C)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val items = listOf(
                    Screen.Home,
                    Screen.Records,
                    Screen.Accounts,
                    Screen.Menu
                )
                items.forEach { screen ->
                    if (screen.route == "Accounts") {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.route) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        selectedContentColor = Color(0xFF7F52FF),
                        unselectedContentColor = Color.Gray,
                        alwaysShowLabel = true
                    )
                    if (screen.route == "Records") {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { DashboardScreen(viewModel = viewModel, onAllBudgetsClicked = { navController.navigate(NavRoutes.BUDGETS) }, onStatisticsClicked = { navController.navigate(NavRoutes.STATISTICS) }) }
            composable(Screen.Records.route) {
                RecordsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onAddRecord = { navController.navigate(NavRoutes.ADD_TRANSACTION) }
                )
            }
            composable(Screen.Accounts.route) {
                AccountsScreen(viewModel = viewModel)
            }
            composable(NavRoutes.ADD_TRANSACTION) {
                AddTransactionScreen(viewModel = viewModel) {
                    navController.popBackStack()
                }
            }
            composable(NavRoutes.BUDGETS) {
                BudgetsScreen(viewModel = viewModel) {
                    navController.popBackStack()
                }
            }
            composable(NavRoutes.STATISTICS) {
                StatisticsScreen(viewModel = viewModel) {
                    navController.popBackStack()
                }
            }
        }
    }
}

sealed class Screen(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("Home", Icons.Default.Home)
    object Records : Screen("Records", Icons.Default.Description)
    object Accounts : Screen("Accounts", Icons.Default.AccountBalanceWallet)
    object Menu : Screen("Menu", Icons.Default.Menu)
}
