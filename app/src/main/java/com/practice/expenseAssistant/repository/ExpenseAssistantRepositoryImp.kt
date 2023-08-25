package com.practice.expenseAssistant.repository

import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.database.dao.TransactionDao
import com.practice.expenseAssistant.repository.database.entities.Transaction
import com.practice.expenseAssistant.utils.CategoryType
import com.practice.expenseAssistant.utils.ExpenseType
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class ExpenseAssistantRepositoryImp @Inject constructor(
    private val transactionDao: TransactionDao,
) : ExpenseAssistantRepository {

    private var selectedDate: LocalDate = LocalDate.now()

    private lateinit var user: UserModel

    private lateinit var calendarData: CalendarDataModel

    private val currentMonth: LocalDate = LocalDate.of(
        LocalDate.now().year,
        LocalDate.now().month,
        1,
    )

    private var categoryType: CategoryType = CategoryType.EXPENSE
    private var category: String = ExpenseType.OTHERS.name
    private var totalExpenseOfMonth: Double = 0.0

    private val balanceModel = BalanceModel(
        openingBalance = 2000.0,
        closingBalance = 800.0,
        totalExpense = 1200.0,
    )

    private val _calender = MutableStateFlow<List<CalendarDateModel>>(listOf())
    private val calender = _calender.asStateFlow()

    override fun setUser(user: UserModel) {
        this.user = user
    }

    override fun setCalenderData(data: CalendarDataModel) {
        calendarData = data
    }

    override fun setCategoryType(categoryType: CategoryType) {
        this.categoryType = categoryType
    }

    override fun setCategory(category: String) {
        this.category = category
    }

    override fun setTotalExpenseOfMonth(expense: Double) {
        totalExpenseOfMonth = expense
    }

    override fun updateCategoryAndType(category: String, categoryType: CategoryType) {
        this.category = category
        this.categoryType = categoryType
    }

    override suspend fun updateSelectedDate(date: LocalDate) {
        selectedDate = date
    }

    override suspend fun updateCalendar(calendar: List<CalendarDateModel>) {
        _calender.value = calendar
        _calender.emit(calendar)
    }

    override suspend fun addTransaction(transaction: TransactionModel, bankAccount: BankAccount) {
        user = user.copy(transactions = addTransaction(transaction))
        transactionDao.addTransaction(
            Transaction(
                amount = transaction.amount,
                note = transaction.note,
                backAccount = bankAccount,
                categoryType = transaction.categoryType,
                category = transaction.category.toString(),
                date = transaction.date,
                time = transaction.time
            )
        )
    }

    override suspend fun removeTransaction(transaction: TransactionModel) {
        transactionDao.removeTransaction(date = transaction.date, time = transaction.time)
    }

    override fun getTransaction(date: LocalDate, time: LocalTime): TransactionModel? {
        return user.transactions[date]?.find { it.time == time }
    }

    override fun getTransactionsByDate(date: LocalDate): List<TransactionModel>? {
        return user.transactions[date]
    }

    override fun getAllTransactions(): Map<LocalDate, List<TransactionModel>> {
        return user.transactions
    }

    override fun getTodayDate(): LocalDate = LocalDate.now()
    override fun getCurrentMonth(): LocalDate = currentMonth
    override fun getMonthCalenderModel(): CalendarDataModel = calendarData
    override fun getCalender(): StateFlow<List<CalendarDateModel>> = calender
    override fun getUser(): UserModel = user
    override fun getCategoryType(): CategoryType = categoryType
    override fun getCategory(): String = category
    override fun getBalance(): BalanceModel = balanceModel
    override fun getTotalExpenseOfMonth(): Double = totalExpenseOfMonth
    override fun getSelectedDate(): LocalDate = selectedDate
    override fun getTransactionsOfSelectedDate(): List<TransactionModel>? =
        user.transactions[selectedDate]

    private fun addTransaction(transaction: TransactionModel): Map<LocalDate, List<TransactionModel>> {
        val tempTransactions = user.transactions.toMutableMap()
        if (tempTransactions[transaction.date] == null) {
            tempTransactions[transaction.date] = listOf(transaction)
        } else {
            val temp = tempTransactions[transaction.date]!!.toMutableList()
            temp.add(transaction)
            tempTransactions[transaction.date] = temp
        }
        return tempTransactions
    }
}