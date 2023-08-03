package com.practice.expenseAssistant.data

data class ExpenseModel(
    val expenseType: ExpenseType,
    val expenseNote: String,
    val expense: Int
)
