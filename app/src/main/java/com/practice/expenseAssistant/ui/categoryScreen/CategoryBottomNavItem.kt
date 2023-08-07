package com.practice.expenseAssistant.ui.categoryScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.graphics.vector.ImageVector

sealed class CategoryBottomNavItem(
    var title: String,
    var icon: ImageVector,
    var screenRoute: String
) {

    object Expense : CategoryBottomNavItem(
        title = "Expense",
        icon = Icons.Filled.ArrowBack,
        screenRoute = "expense"
    )

    object Income : CategoryBottomNavItem(
        title = "Income",
        icon = Icons.Filled.ArrowForward,
        screenRoute = "income"
    )
}
