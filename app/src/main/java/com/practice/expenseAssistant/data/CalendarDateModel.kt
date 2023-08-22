package com.practice.expenseAssistant.data

import java.time.LocalDate

data class CalendarDateModel(
    val id: Int,
    val date: LocalDate,
    val isSelected: Boolean,
    val isCurrentMonthDate: Boolean,
    val todayTotalExpense: Double = 0.0,
    val todayTotalIncome: Double = 0.0,
    val todayTransactions: List<TransactionModel>? = null,
)