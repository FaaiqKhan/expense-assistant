package com.practice.expenseAssistant.data

import com.practice.expenseAssistant.utils.CategoryType
import java.time.LocalDate
import java.time.LocalTime

data class ExpenseModel(
    val categoryType: CategoryType,
    val category: Any,
    val expenseNote: String,
    val expense: Int,
    val date: LocalDate,
    val time: LocalTime
)
