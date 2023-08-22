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
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.CategoryType

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    onSelect: (category: String, categoryType: CategoryType) -> Unit,
) {
    var tabState by remember { mutableStateOf(0) }

    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        AnimatedVisibility(
            visible = tabState == 0,
            enter = slideInHorizontally(),
            exit = scaleOut()
        ) {
            ExpenseCategories(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(all = dimensionResource(id = R.dimen.screen_content_padding)),
                onSelect = { onSelect(it, CategoryType.EXPENSE) }
            )
        }
        AnimatedVisibility(
            visible = tabState == 1,
            enter = slideInHorizontally(),
            exit = scaleOut()
        ) {
            IncomeCategories(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(all = dimensionResource(id = R.dimen.screen_content_padding)),
                onSelect = { onSelect(it, CategoryType.INCOME) }
            )
        }
        TabRow(selectedTabIndex = tabState) {
            CategoryType.values().forEachIndexed { index, item ->
                Tab(
                    text = { Text(text = item.name) },
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
        CategoryScreen(modifier = Modifier.fillMaxWidth(), onSelect = {_, _ -> })
    }
}