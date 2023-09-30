package com.practice.expenseAssistant.repository

import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.database.entities.CashFlow
import com.practice.expenseAssistant.utils.CategoryType
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface ExpenseAssistantRepository {

    fun setUser(user: UserModel)
    fun setCategoryType(categoryType: CategoryType)
    fun setCategory(category: String)
    suspend fun setMonthCashFLow(cashFlow: MonthCashFlow)
    fun updateCategoryAndType(category: String, categoryType: CategoryType)
    suspend fun updateSelectedDate(date: LocalDate)
    suspend fun updateCalendar(calendar: List<CalendarDateModel>)
    suspend fun fetchAllTransactionsOfUser(userId: Int): Map<LocalDate, List<TransactionModel>>
    suspend fun addTransaction(transaction: TransactionModel, bankAccount: BankAccount)
    suspend fun removeTransaction(transaction: TransactionModel)
    fun getUser(): UserModel
    fun getTransactionsByDate(date: LocalDate): List<TransactionModel>?
    fun getAllTransactions(): Map<LocalDate, List<TransactionModel>>
    fun getCurrentMonth(): LocalDate
    fun getCalender(): StateFlow<List<CalendarDateModel>>
    fun getCategoryType(): CategoryType
    fun getCategory(): String
    fun getTotalExpenseOfMonth(): Double
    fun getTotalIncomeOfMonth(): Double
    fun getSelectedDate(): LocalDate
    fun getTransactionsOfSelectedDate(): List<TransactionModel>?
    fun getMonthCashFlow(): StateFlow<MonthCashFlow>
    fun getCashFlowOfMonth(localDate: LocalDate): MonthCashFlow
    suspend fun insertCashFlowIntoDb(cashFlow: CashFlow)
    suspend fun fetchCashFlowOfMonth(month: Int, year: Int): MonthCashFlow
    suspend fun updateMonthCashFlow(cashFlow: MonthCashFlow, isExpense: Boolean)
    suspend fun fetchAllTransactionsOfMonthAndYear(
        month: Int,
        year: Int,
    ): List<TransactionModel>

    suspend fun fetchAllTransactionsOfTypeWithMonthAndYear(
        month: Int,
        year: Int,
        categoryType: CategoryType
    ): List<TransactionModel>
}