package com.practice.expenseAssistant.utils

import androidx.annotation.StringRes
import com.practice.expenseAssistant.R

enum class NavigationItemType {
    HOME,
    PROFILE,
    INFO
}

enum class Screens(@StringRes val title: Int) {
    LOGIN(title = R.string.login),
    HOME(title = R.string.app_name),
    CATEGORY(title = R.string.category),
    TRANSACTION(title = R.string.transaction),
    PROFILE(title = R.string.profile),
    VIEW_ALL(title = R.string.view_all),
    EXPENSE(title = R.string.expense),
    INCOME(title = R.string.income)
}

enum class CategoryType {
    EXPENSE,
    INCOME
}