package com.practice.expenseAssistant.data

data class ExpenseModel(
    val date: Int,
    val expense: String? = null,
    val isSelected: Boolean,
    val isCurrentMonthDate: Boolean
)