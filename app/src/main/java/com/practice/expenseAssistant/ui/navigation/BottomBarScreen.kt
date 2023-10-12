package com.practice.expenseAssistant.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.practice.expenseAssistant.utils.Screens

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val focusedIcon: ImageVector
) {
    object Home: BottomBarScreen(
        route = Screens.HOME.name,
        title = "Home",
        icon = Icons.Default.Home,
        focusedIcon = Icons.Default.Home
    )

    object Profile: BottomBarScreen(
        route = Screens.PROFILE.name,
        title = "Profile",
        icon = Icons.Default.Home,
        focusedIcon = Icons.Default.Home
    )
}
