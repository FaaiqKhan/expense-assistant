package com.practice.expenseAssistant.ui

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.NavigationBarItem
import com.practice.expenseAssistant.ui.categoryScreen.*
import com.practice.expenseAssistant.ui.common.BottomNavigationBar
import com.practice.expenseAssistant.ui.homeScreen.ExpenseAssistantViewModel
import com.practice.expenseAssistant.ui.homeScreen.HomeScreen
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.ui.transactionScreen.TransactionScreen
import com.practice.expenseAssistant.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAssistantApp(
    expenseAssistant: ExpenseAssistantViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.HOME.name
    )

    val bottomNavigationItem = listOf(
        NavigationBarItem(
            itemType = NavigationItemType.HOME,
            icon = Icons.Default.Home,
            text = stringResource(id = R.string.home)
        ),
        NavigationBarItem(
            itemType = NavigationItemType.PROFILE,
            icon = Icons.Default.Person,
            text = stringResource(id = R.string.profile)
        ),
        NavigationBarItem(
            itemType = NavigationItemType.INFO,
            icon = Icons.Default.Info,
            text = stringResource(id = R.string.information)
        )
    )

    Scaffold(
        topBar = {
            ExpenseAssistantTopBar(
                screenTitleId = currentScreen.title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateBack = navController::popBackStack
            )
        },
        bottomBar = {
            if (currentScreen.name == Screens.HOME.name) {
                ExpenseAssistantBottomBar(items = bottomNavigationItem)
            }
        },
        floatingActionButton = {
            if (currentScreen.name == Screens.HOME.name)
                ExpenseAssistantActionButton(navController)
        }
    ) {
        NavigationHost(
            modifier = Modifier.padding(it),
            navController = navController,
            expenseAssistant = expenseAssistant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAssistantTopBar(
    modifier: Modifier = Modifier,
    @StringRes screenTitleId: Int,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = screenTitleId),
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
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun ExpenseAssistantBottomBar(modifier: Modifier = Modifier, items: List<NavigationBarItem>) {
    val bottomNavigationContentDescription = stringResource(id = R.string.navigation_bottom)
    BottomNavigationBar(
        currentItem = NavigationItemType.HOME,
        navigationItemContentList = items,
        onTabPress = {},
        modifier = modifier.testTag(bottomNavigationContentDescription)
    )
}

@Composable
fun ExpenseAssistantActionButton(navController: NavHostController) {
    FloatingActionButton(onClick = {
        navController.navigate(Screens.CATEGORY.name)
//        expenseAssistant.addExpense(
//            selectedDate = expenseAssistant.selectedDate,
//            expenseModel = ExpenseModel(
//                expense = 2300,
//                expenseType = ExpenseType.BILL,
//                expenseNote = "Water utilities with maintenance"
//            )
//        )
    }) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_expense)
        )
    }
}

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    expenseAssistant: ExpenseAssistantViewModel
) {
    NavHost(navController = navController, startDestination = Screens.HOME.name) {
        composable(route = Screens.HOME.name) {
            HomeScreen(expenseAssistant = expenseAssistant, modifier = modifier)
        }
        composable(route = Screens.CATEGORY.name) {
            CategoryScreen(modifier = modifier.fillMaxHeight())
        }
        composable(route = Screens.TRANSACTION.name) {
            TransactionScreen(modifier = modifier, onTransaction = {})
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