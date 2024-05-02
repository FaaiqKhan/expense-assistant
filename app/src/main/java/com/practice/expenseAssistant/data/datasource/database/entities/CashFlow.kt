package com.practice.expenseAssistant.data.datasource.database.entities

import androidx.room.*

@Entity("cash_flow")
data class CashFlow(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("month")
    val month: Int,
    @ColumnInfo("year")
    val year: Int,
    @ColumnInfo("month_income")
    val income: Double = 0.0,
    @ColumnInfo("month_expense")
    val expense: Double = 0.0,
    @ColumnInfo("month_opening_amount")
    val openingAmount: Double,
    @ColumnInfo("month_closing_amount")
    val closingAmount: Double,
    @ColumnInfo("user_id")
    val userId: Int,
)