package com.practice.expenseAssistant.data.datasource.database.dao

import androidx.room.*
import com.practice.expenseAssistant.data.datasource.database.entities.CashFlow

@Dao
interface CashFlowDao {

    @Insert
    suspend fun insertCashFlow(cashFlow: CashFlow)

    @Update
    suspend fun updateCashFlow(cashFlow: CashFlow)

    @Delete
    suspend fun deleteCashFlow(cashFlow: CashFlow)

    @Query("UPDATE cash_flow SET month_income = :amount WHERE month = :month AND year = :year")
    suspend fun updateIncome(month: Int, year: Int, amount: Double)

    @Query("UPDATE cash_flow SET month_expense = :amount WHERE month = :month AND year = :year")
    suspend fun updateExpense(month: Int, year: Int, amount: Double)

    @Query("UPDATE cash_flow SET month_opening_amount = :amount WHERE month = :month AND year = :year")
    suspend fun updateOpeningBalance(month: Int, year: Int, amount: Double)

    @Query("UPDATE cash_flow SET month_closing_amount = :amount WHERE month = :month AND year = :year")
    suspend fun updateClosingBalance(month: Int, year: Int, amount: Double)

    @Query("SELECT * FROM cash_flow")
    suspend fun getAllCashFlows(): List<CashFlow>
    
    @Query("SELECT * FROM cash_flow where month = :month AND year = :year")
    suspend fun getCashFlowOfMonth(month: Int, year: Int): CashFlow?
}