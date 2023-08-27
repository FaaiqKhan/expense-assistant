package com.practice.expenseAssistant.utils

import android.os.Parcelable
import androidx.annotation.StringRes
import com.practice.expenseAssistant.R
import kotlinx.parcelize.Parcelize

enum class NavigationItemType {
    HOME,
    PROFILE,
    INFO
}

enum class Screens(@StringRes val title: Int) {
    LOGIN(title = R.string.app_name),
    HOME(title = R.string.app_name),
    CATEGORY(title = R.string.category),
    TRANSACTION(title = R.string.transaction),
    PROFILE(title = R.string.profile),
    VIEW_ALL(title = R.string.view_all),
    EXPENSE(title = R.string.expense),
    INCOME(title = R.string.income)
}

@Parcelize
enum class CategoryType: Parcelable {
    EXPENSE,
    INCOME
}

enum class CurrencyType(val icon: String) {
    Euro(icon = "€"),
    Dollar(icon = "$"),
    PakistaniRupee(icon = "₨"),
    Ponds(icon = "£"),
    Lira(icon = "₤"),
}