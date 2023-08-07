package com.practice.expenseAssistant.ui.categoryScreen

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(modifier: Modifier = Modifier) {
    var tabState by remember { mutableStateOf(0) }
    val titles = listOf("Expense", "Income")

    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        AnimatedVisibility(
            visible = tabState == 0,
            modifier = modifier,
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
            modifier = modifier,
            enter = slideInHorizontally(),
        ) {
            IncomeCategories(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding))
            )
        }
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
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTransactionCategory() {
    ExpenseAssistantTheme {
        CategoryScreen()
    }
}