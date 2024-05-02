package com.practice.expenseAssistant

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.practice.expenseAssistant.ui.aboutScreen.AboutScreen
import com.practice.expenseAssistant.ui.categoryScreen.CategoryScreen
import com.practice.expenseAssistant.ui.categoryScreen.CategoryScreenViewModel
import com.practice.expenseAssistant.ui.expensesScreen.ExpensesScreen
import com.practice.expenseAssistant.ui.homeScreen.HomeScreen
import com.practice.expenseAssistant.ui.incomesScreen.IncomesScreen
import com.practice.expenseAssistant.ui.loginScreen.LoginScreen
import com.practice.expenseAssistant.ui.profileScreen.ProfileScreen
import com.practice.expenseAssistant.ui.splashScreen.SplashScreen
import com.practice.expenseAssistant.ui.statementScreen.StatementScreen
import com.practice.expenseAssistant.ui.theme.spacing
import com.practice.expenseAssistant.ui.transactionScreen.TransactionScreen
import com.practice.expenseAssistant.utils.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAssistantApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        content = { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = Screens.SPLASH.name
            ) {
                composable(route = Screens.SPLASH.name) {
                    SplashScreen(modifier = Modifier.fillMaxSize(), navController = navController)
                }
                composable(route = Screens.LOGIN.name) {
                    LoginScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .padding(MaterialTheme.spacing.large),
                        navController = navController
                    )
                }
                composable(route = Screens.HOME.name) {
                    HomeScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .padding(MaterialTheme.spacing.large),
                        controller = navController,
                        onTransactionSelect = { transaction ->
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "transaction",
                                transaction
                            )
                            navController.navigate(Screens.TRANSACTION.name)
                        },
                    )
                }
                composable(route = Screens.CATEGORY.name) {
                    val categoryViewModel: CategoryScreenViewModel = hiltViewModel()
                    CategoryScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxHeight(),
                        onSelect = { category, categoryType ->
                            categoryViewModel.updateCategory(
                                type = categoryType,
                                category = category
                            )
                            navController.navigate(Screens.TRANSACTION.name) { popUpTo(Screens.HOME.name) }
                        }
                    )
                }
                composable(route = Screens.TRANSACTION.name) {
                    TransactionScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(MaterialTheme.spacing.large),
                        navController = navController,
                    )
                }
                composable(route = Screens.MONTHLY_EXPENSES.name) {
                    ExpensesScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                    )
                }
                composable(route = Screens.MONTHLY_INCOMES.name) {
                    IncomesScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                    )
                }
                composable(route = Screens.PROFILE.name) {
                    ProfileScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(MaterialTheme.spacing.large)
                    )
                }
                composable(route = Screens.ABOUT.name) {
                    AboutScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(MaterialTheme.spacing.large)
                    )
                }
                composable(route = Screens.MONTHLY_STATEMENT.name) {
                    StatementScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                    )
                }
            }
        }
    )
}