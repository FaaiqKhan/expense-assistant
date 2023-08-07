package com.practice.expenseAssistant.data

import androidx.compose.ui.graphics.vector.ImageVector
import com.practice.expenseAssistant.utils.NavigationItemType

data class NavigationBarItem(
    val itemType: NavigationItemType,
    val icon: ImageVector,
    val text: String
)