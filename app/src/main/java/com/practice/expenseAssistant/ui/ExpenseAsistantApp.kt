package com.practice.expenseAssistant.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.ui.aboutScreen.AboutScreen
import com.practice.expenseAssistant.ui.categoryScreen.*
import com.practice.expenseAssistant.ui.common.NoRippleButton
import com.practice.expenseAssistant.ui.expensesScreen.ExpensesScreen
import com.practice.expenseAssistant.ui.homeScreen.HomeScreen
import com.practice.expenseAssistant.ui.incomesScreen.IncomesScreen
import com.practice.expenseAssistant.ui.loginScreen.LoginScreen
import com.practice.expenseAssistant.ui.profileScreen.ProfileScreen
import com.practice.expenseAssistant.ui.statementScreen.StatementScreen
import com.practice.expenseAssistant.ui.transactionScreen.TransactionScreen
import com.practice.expenseAssistant.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAssistantApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold {
        NavigationHost(modifier = Modifier.padding(it), navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAssistantTopBar(
    modifier: Modifier = Modifier,
    screen: Screens,
    controller: NavHostController,
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = screen.title),
                style = MaterialTheme.typography.displayLarge
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        navigationIcon = {
            if (controller.previousBackStackEntry != null) {
                IconButton(onClick = {
                    if (screen == Screens.TRANSACTION) {
                        controller.previousBackStackEntry?.savedStateHandle
                            ?.remove<TransactionModel>("transaction")
                    }
                    controller.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (screen == Screens.HOME) {
                IconButton(onClick = { controller.navigate(Screens.CATEGORY.name) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_expense),
                    )
                }
                IconButton(onClick = { isMenuOpen = !isMenuOpen }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(id = R.string.menu),
                    )
                }
                DropdownMenu(expanded = isMenuOpen, onDismissRequest = { isMenuOpen = false }) {
                    NoRippleButton(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                            .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                        text = R.string.statements
                    ) {
                        isMenuOpen = false
                        controller.navigate(Screens.MONTHLY_STATEMENT.name)
                    }
                    NoRippleButton(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                            .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                        text = R.string.view_all_expenses,
                    ) {
                        isMenuOpen = false
                        controller.navigate(Screens.MONTHLY_EXPENSES.name)
                    }
                    NoRippleButton(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                            .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                        text = R.string.view_all_income,
                    ) {
                        isMenuOpen = false
                        controller.navigate(Screens.MONTHLY_INCOMES.name)
                    }
                    NoRippleButton(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                            .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                        text = R.string.profile,
                    ) {
                        isMenuOpen = false
                        controller.navigate(Screens.PROFILE.name)
                    }
                    NoRippleButton(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.eight_dp)),
                        text = R.string.about,
                    ) {
                        isMenuOpen = false
                        controller.navigate(Screens.ABOUT.name)
                    }
                }
            }
        }
    )
}

@Composable
fun NavigationHost(modifier: Modifier, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.LOGIN.name) {
        composable(route = Screens.LOGIN.name) {
            LoginScreen(
                modifier = modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.screen_content_padding)),
                navController = navController
            )
        }
        composable(route = Screens.HOME.name) {
            HomeScreen(
                modifier = modifier.fillMaxSize(),
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
                modifier = modifier.fillMaxHeight(),
                onSelect = { category, categoryType ->
                    categoryViewModel.updateCategory(type = categoryType, category = category)
                    navController.navigate(Screens.TRANSACTION.name) { popUpTo(Screens.HOME.name) }
                }
            )
        }
        composable(route = Screens.TRANSACTION.name) {
            TransactionScreen(
                modifier = modifier.padding(dimensionResource(id = R.dimen.screen_content_padding)),
                navController = navController,
            )
        }
        composable(route = Screens.MONTHLY_EXPENSES.name) {
            ExpensesScreen(modifier = modifier)
        }
        composable(route = Screens.MONTHLY_INCOMES.name) {
            IncomesScreen(modifier = modifier)
        }
        composable(route = Screens.PROFILE.name) {
            ProfileScreen(modifier = modifier.padding(dimensionResource(id = R.dimen.screen_content_padding)))
        }
        composable(route = Screens.ABOUT.name) {
            AboutScreen(modifier = modifier.padding(dimensionResource(id = R.dimen.screen_content_padding)))
        }
        composable(route = Screens.MONTHLY_STATEMENT.name) {
            StatementScreen(modifier = modifier)
        }
    }
}