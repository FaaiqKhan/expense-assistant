package com.practice.expenseAssistant.repository

import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.utils.CategoryType
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface ExpenseAssistantRepository {

    fun setUser(user: UserModel)
    fun setDate(date: LocalDate)
    fun setCalenderData(data: CalendarDataModel)
    fun setCategoryType(categoryType: CategoryType)
    fun setCategory(category: String)
    fun setTotalExpenseOfMonth(expense: Double)
    fun updateCategoryAndType(category: String, categoryType: CategoryType)
    fun updateSelectedDate(date: LocalDate)
    suspend fun updateCalendar(calendar: List<CalendarDateModel>)
    suspend fun addTransaction(transaction: TransactionModel, bankAccount: BankAccount)
    suspend fun removeTransaction(transaction: TransactionModel)
    fun getUser(): UserModel
    fun getTransaction(date: LocalDate, transactionId: Int): TransactionModel?
    fun getTransactionsByDate(date: LocalDate): List<TransactionModel>?
    fun getAllTransactions(): Map<LocalDate, List<TransactionModel>>
    fun getTodayDate(): LocalDate
    fun getCurrentMonth(): LocalDate
    fun getMonthCalenderModel(): CalendarDataModel
    fun getCalender(): StateFlow<List<CalendarDateModel>>
    fun getCategoryType(): CategoryType
    fun getCategory(): String
    fun getBalance(): BalanceModel
    fun getTotalExpenseOfMonth(): Double
    fun getSelectedDate(): LocalDate
}