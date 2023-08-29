package com.practice.expenseAssistant.repository

import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.database.dao.CashFlowDao
import com.practice.expenseAssistant.repository.database.dao.TransactionDao
import com.practice.expenseAssistant.repository.database.entities.CashFlow
import com.practice.expenseAssistant.repository.database.entities.Transaction
import com.practice.expenseAssistant.utils.CategoryType
import com.practice.expenseAssistant.utils.ExpenseType
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class ExpenseAssistantRepositoryImp @Inject constructor(
    private val cashFlowDao: CashFlowDao,
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

    private val _calender = MutableStateFlow<List<CalendarDateModel>>(listOf())
    private val calender = _calender.asStateFlow()

    private val userCashFlow: Map<LocalDate, MonthCashFlow> = mapOf()

    private val _monthCashFlow = MutableStateFlow(
        MonthCashFlow(
            income = 0.0,
            expense = 0.0,
            openingAmount = 0.0,
            closingAmount = 0.0
        )
    )
    private val monthCashFlow = _monthCashFlow.asStateFlow()

    private var abc = mapOf(
        selectedDate to MonthCashFlow(
            income = 0.0,
            expense = 0.0,
            openingAmount = 0.0,
            closingAmount = 0.0
        )
    )

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

    override fun setMonthCashFLow(cashFlow: Map<LocalDate, MonthCashFlow>) {
        _monthCashFlow.update {
            cashFlow[selectedDate] ?: MonthCashFlow(
                income = 0.0,
                expense = 0.0,
                openingAmount = 0.0,
                closingAmount = 0.0
            )
        }
        abc = cashFlow
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
        user = user.copy(transactions = deleteTransaction(transaction))
        transactionDao.removeTransaction(date = transaction.date, time = transaction.time)
    }

    override fun getTransaction(date: LocalDate, time: LocalTime): TransactionModel? {
        return user.transactions[date]?.find { it.time == time }
    }

    override fun getTransactionsByDate(date: LocalDate): List<TransactionModel>? {
        return user.transactions[date]
    }

    override fun getAllTransactions(): Map<LocalDate, List<TransactionModel>> = user.transactions
    override fun getTodayDate(): LocalDate = LocalDate.now()
    override fun getCurrentMonth(): LocalDate = currentMonth
    override fun getMonthCalenderModel(): CalendarDataModel = calendarData
    override fun getCalender(): StateFlow<List<CalendarDateModel>> = calender
    override fun getUser(): UserModel = user
    override fun getCategoryType(): CategoryType = categoryType
    override fun getCategory(): String = category
    override fun getTotalExpenseOfMonth(): Double = totalExpenseOfMonth
    override fun getSelectedDate(): LocalDate = selectedDate
    override fun getTransactionsOfSelectedDate() = user.transactions[selectedDate]
    override fun getMonthCashFlow(): StateFlow<MonthCashFlow> = monthCashFlow

    override suspend fun insertCashFlowIntoDb(cashFlow: CashFlow) =
        cashFlowDao.insertCashFlow(cashFlow)

    override suspend fun getCashFlowFromDb(): List<CashFlow> = cashFlowDao.getAllCashFlows()

    override fun getAbc() = abc

    override fun updateMonthCashFlow(cashFlow: MonthCashFlow) {
        _monthCashFlow.value = cashFlow
    }

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

    private fun deleteTransaction(transaction: TransactionModel): Map<LocalDate, List<TransactionModel>> {
        val allTransactions = user.transactions.toMutableMap()
        val transactions = allTransactions[transaction.date]!!.toMutableList()
        if (transactions.size == 1) {
            allTransactions.remove(transaction.date)
        } else {
            transactions.removeIf { it.date == transaction.date && it.time == transaction.time }
            allTransactions[transaction.date] = transactions
        }
        return allTransactions
    }
}