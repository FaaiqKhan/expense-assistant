package com.practice.expenseAssistant.ui.common

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.practice.expenseAssistant.data.NavigationBarItem
import com.practice.expenseAssistant.utils.NavigationItemType

@Composable
fun BottomNavigationBar(
    currentItem: NavigationItemType,
    onTabPress: (bottomNavItemType: NavigationItemType) -> Unit,
    navigationItemContentList: List<NavigationBarItem>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        navigationItemContentList.forEach {
            NavigationBarItem(
                selected = currentItem == it.itemType,
                onClick = { onTabPress(it.itemType) },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.text
                    )
                }
            )
        }
    }
}