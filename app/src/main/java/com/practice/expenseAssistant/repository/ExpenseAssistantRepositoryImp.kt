package com.practice.expenseAssistant.repository

import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.database.dao.TransactionDao
import com.practice.expenseAssistant.repository.database.entities.Transaction
import com.practice.expenseAssistant.utils.CategoryType
import com.practice.expenseAssistant.utils.ExpenseType
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

class ExpenseAssistantRepositoryImp @Inject constructor(
    private val transactionDao: TransactionDao,
) : ExpenseAssistantRepository {

    private var today: LocalDate = LocalDate.now()
    private var selectedDate: LocalDate = LocalDate.now()

    private lateinit var user: UserModel

    private lateinit var calendarData: CalendarDataModel

    private val currentMonth: LocalDate = LocalDate.of(today.year, today.month, 1)

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

    override fun setDate(date: LocalDate) {
        today = date
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

    override fun updateSelectedDate(date: LocalDate) {
        selectedDate = date
    }

    override suspend fun updateCalendar(calendar: List<CalendarDateModel>) {
        _calender.value = calendar
        _calender.emit(calendar)
    }

    override suspend fun addTransaction(transaction: TransactionModel, bankAccount: BankAccount) {
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
        transactionDao.removeTransaction(transaction.transactionId)
    }

    override fun getTransaction(date: LocalDate, transactionId: Int): TransactionModel? {
        val transactions = user.transactions[date]
        return transactions?.find { it.transactionId == transactionId }
    }

    override fun getTransactionsByDate(date: LocalDate): List<TransactionModel>? {
        return user.transactions[date]
    }

    override fun getAllTransactions(): Map<LocalDate, List<TransactionModel>> = user.transactions
    override fun getTodayDate(): LocalDate = today
    override fun getCurrentMonth(): LocalDate = currentMonth
    override fun getMonthCalenderModel(): CalendarDataModel = calendarData
    override fun getCalender(): StateFlow<List<CalendarDateModel>> = calender
    override fun getUser(): UserModel = user
    override fun getCategoryType(): CategoryType = categoryType
    override fun getCategory(): String = category
    override fun getBalance(): BalanceModel = balanceModel
    override fun getTotalExpenseOfMonth(): Double = totalExpenseOfMonth
    override fun getSelectedDate(): LocalDate = selectedDate
}