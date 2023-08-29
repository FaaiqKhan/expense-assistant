package com.practice.expenseAssistant.data

data class MonthCashFlow(
    val income: Double = 0.0,
    val expense: Double = 0.0,
    val openingAmount: Double,
    val closingAmount: Double,
)
