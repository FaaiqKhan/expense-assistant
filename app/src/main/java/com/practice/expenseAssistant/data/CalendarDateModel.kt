package com.practice.expenseAssistant.data

import java.time.LocalDate

data class CalendarDateModel(
    val id: Int,
    val date: LocalDate,
    val isSelected: Boolean,
    val isCurrentMonthDate: Boolean,
    val transactionModel: List<TransactionModel> = listOf(),
)