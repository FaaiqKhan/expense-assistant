package com.practice.expenseAssistant.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.categoryScreen.*
import com.practice.expenseAssistant.ui.homeScreen.HomeScreen
import com.practice.expenseAssistant.ui.loginScreen.LoginScreen
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.ui.transactionScreen.TransactionScreen
import com.practice.expenseAssistant.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAssistantApp(
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.HOME.name
    )

    Scaffold(
        topBar = {
            ExpenseAssistantTopBar(
                screen = currentScreen,
                controller = navController,
                onAddTransaction = { navController.navigate(Screens.CATEGORY.name) },
                onViewMenu = {}
            )
        },
    ) {
        NavigationHost(modifier = Modifier.padding(it), navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAssistantTopBar(
    modifier: Modifier = Modifier,
    screen: Screens,
    controller: NavHostController,
    onAddTransaction: () -> Unit,
    onViewMenu: () -> Unit,
) {
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
                IconButton(onClick = controller::popBackStack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (screen == Screens.HOME) {
                IconButton(onClick = onAddTransaction) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_expense),
                    )
                }
                IconButton(onClick = onViewMenu) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = stringResource(id = R.string.menu),
                    )
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
                onTransactionSelect = {

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
                onNavigate = {
                    if (it == null) navController.popBackStack()
                    else navController.navigate(it)
                },
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExpenseAssistantApp() {
    ExpenseAssistantTheme {
        ExpenseAssistantApp()
    }
}