package com.practice.expenseAssistant.utils

import android.os.Parcelable
import androidx.annotation.StringRes
import com.practice.expenseAssistant.R
import kotlinx.parcelize.Parcelize

enum class NavigationBarItems {
    HOME,
    PROFILE,
    INFO
}

enum class Screens(@StringRes val title: Int) {
    SPLASH(title = R.string.splash),
    LOGIN(title = R.string.app_name),
    HOME(title = R.string.app_name),
    CATEGORY(title = R.string.category),
    TRANSACTION(title = R.string.transaction),
    PROFILE(title = R.string.profile),
    MONTHLY_STATEMENT(title = R.string.monthly_statement),
    MONTHLY_EXPENSES(title = R.string.monthly_expenses),
    MONTHLY_INCOMES(title = R.string.monthly_incomes),
    ABOUT(title = R.string.about),
}

@Parcelize
enum class CategoryType : Parcelable {

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