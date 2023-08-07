package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.data.CategoryNavBarItem
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.CategoryType

@Composable
fun CategoryBottomNavBar(
    modifier: Modifier = Modifier,
    navItems: List<CategoryNavBarItem>,
    onTabPress: (type: CategoryType) -> Unit,
) {
    var currentItem by remember { mutableStateOf(CategoryType.EXPENSE) }

    NavigationBar(modifier = modifier) {
        navItems.forEach {
            NavigationBarItem(
                selected = currentItem == it.type,
                onClick = {
                    currentItem = it.type
                    onTabPress(it.type)
                },
                icon = { Text(text = it.text) },
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCategoryBottomNavBar() {
    ExpenseAssistantTheme {
        CategoryBottomNavBar(modifier = Modifier.fillMaxWidth(), navItems = listOf(
            CategoryNavBarItem(type = CategoryType.EXPENSE, text = "Expense"),
            CategoryNavBarItem(type = CategoryType.INCOME, text = "Income")
        ), onTabPress = {})
    }
}