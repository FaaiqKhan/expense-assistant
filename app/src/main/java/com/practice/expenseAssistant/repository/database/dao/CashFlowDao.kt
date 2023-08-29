package com.practice.expenseAssistant.repository.database.dao

import androidx.room.*
import com.practice.expenseAssistant.repository.database.entities.CashFlow
import java.time.LocalDate

@Dao
interface CashFlowDao {

    @Insert
    suspend fun insertCashFlow(cashFlow: CashFlow)

    @Update
    suspend fun updateCashFlow(cashFlow: CashFlow)

    @Delete
    suspend fun deleteCashFlow(cashFlow: CashFlow)

    @Query("UPDATE cash_flow SET month_income = :amount WHERE month = :month")
    suspend fun updateIncome(month: LocalDate, amount: Double)

    @Query("UPDATE cash_flow SET month_expense = :amount WHERE month = :month")
    suspend fun updateExpense(month: LocalDate, amount: Double)

    @Query("UPDATE cash_flow SET month_opening_amount = :amount WHERE month = :month")
    suspend fun updateOpeningBalance(month: LocalDate, amount: Double)

    @Query("UPDATE cash_flow SET month_closing_amount = :amount WHERE month = :month")
    suspend fun updateClosingBalance(month: LocalDate, amount: Double)

    @Query("SELECT * FROM cash_flow")
    suspend fun getAllCashFlows(): List<CashFlow>
}