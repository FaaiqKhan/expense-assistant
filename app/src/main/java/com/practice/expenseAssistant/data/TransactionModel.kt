package com.practice.expenseAssistant.data

import com.practice.expenseAssistant.utils.CategoryType
import java.time.LocalDate
import java.time.LocalTime

data class TransactionModel(
    val categoryType: CategoryType,
    val category: Any,
    val note: String,
    val amount: Double,
    val date: LocalDate,
    val time: LocalTime,
)
