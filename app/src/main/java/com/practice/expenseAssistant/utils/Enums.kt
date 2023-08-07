package com.practice.expenseAssistant.utils

import androidx.annotation.StringRes
import com.practice.expenseAssistant.R

enum class NavigationItemType {
    HOME,
    PROFILE,
    INFO
}

enum class Screens(@StringRes val title: Int) {
    HOME(title = R.string.app_name),
    CATEGORY(title = R.string.category),
    TRANSACTION(title = R.string.transaction),
    PROFILE(title = R.string.profile),
    VIEW_ALL(title = R.string.view_all)
}