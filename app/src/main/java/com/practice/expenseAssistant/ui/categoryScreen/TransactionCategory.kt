package com.practice.expenseAssistant.ui.categoryScreen

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCategory(modifier: Modifier = Modifier) {
    var tabState by remember { mutableStateOf(0) }
    val titles = listOf("Expense", "Income")

    Scaffold (
        bottomBar = {
            TabRow(selectedTabIndex = tabState) {
                titles.forEachIndexed { index, item ->
                    Tab(
                        text = { Text(text = item) },
                        selected = tabState == index,
                        onClick = { tabState = index }
                    )
                }
            }
        }
    ) {
        AnimatedVisibility(
            visible = tabState == 0,
            modifier = modifier.padding(it),
            enter = slideInHorizontally(),
            exit = slideOutHorizontally()
        ) {
            ExpenseCategories(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding))
            )
        }
        AnimatedVisibility(
            visible = tabState == 1,
            modifier = modifier.padding(it),
            enter = slideInHorizontally(),
        ) {
            IncomeCategories(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding))
            )
        }
    }
}

//@Composable
//fun NavigationGraph(modifier: Modifier = Modifier, navController: NavHostController) {
//    NavHost(
//        navController = navController,
//        startDestination = CategoryBottomNavItem.Expense.screenRoute
//    ) {
//        composable(route = CategoryBottomNavItem.Expense.screenRoute) {
//            ExpenseCategories(
//                modifier = modifier
//                    .fillMaxSize()
//                    .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding))
//            )
//        }
//        composable(route = CategoryBottomNavItem.Income.screenRoute) {
//            IncomeCategories(
//                modifier = modifier
//                    .fillMaxSize()
//                    .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding))
//            )
//        }
//    }
//}
//
//@Composable
//fun CategoryBottomNavigation(navController: NavHostController) {
//    var selectedItem by remember { mutableIntStateOf(0) }
//
//    val items = listOf(
//        CategoryBottomNavItem.Expense,
//        CategoryBottomNavItem.Income
//    )
//
//    BottomNavigation() {
//        items.forEachIndexed { index, item ->
//            BottomNavigationItem(
//                selected = selectedItem == index,
//                onClick = { selectedItem = index },
//                label = { Text(text = item.title) },
//                icon = {}
//            )
//        }
//    }
//}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTransactionCategory() {
    ExpenseAssistantTheme {
        TransactionCategory()
    }
}