package com.practice.expenseAssistant.data

import com.practice.expenseAssistant.utils.ExpenseType

data class ExpenseModel(
    val expenseType: ExpenseType,
    val expenseNote: String,
    val expense: Int
)
