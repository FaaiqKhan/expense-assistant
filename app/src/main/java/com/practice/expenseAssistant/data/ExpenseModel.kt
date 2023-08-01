package com.practice.expenseAssistant.data

import java.time.LocalDate

data class ExpenseModel(
    val date: LocalDate,
    val expense: String? = null,
    val isSelected: Boolean,
    val isCurrentMonthDate: Boolean
)