package com.practice.expenseAssistant.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IncomeCategoriesIcon {
    val icons: Map<String, ImageVector> = mapOf(
        "Others" to Icons.Filled.ThumbUp,
        "Business Income" to Icons.Filled.Add,
        "Borrow" to Icons.Filled.ArrowDropDown,
        "Gambling" to Icons.Filled.Create,
        "Grants" to Icons.Filled.Edit,
        "Return on Investment" to Icons.Filled.AccountBox,
        "Part-time" to Icons.Filled.ArrowForward,
        "Salary" to Icons.Filled.ExitToApp,
        "Sell" to Icons.Filled.Face,
    )
}