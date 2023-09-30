package com.practice.expenseAssistant.repository

import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.database.dao.CashFlowDao
import com.practice.expenseAssistant.repository.database.dao.TransactionDao
import com.practice.expenseAssistant.repository.database.entities.CashFlow
import com.practice.expenseAssistant.repository.database.entities.Transaction
import com.practice.expenseAssistant.utils.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

class ExpenseAssistantRepositoryImp @Inject constructor(
    private val cashFlowDao: CashFlowDao,
    private val transactionDao: TransactionDao,
) : ExpenseAssistantRepository {

    private var selectedDate: LocalDate = LocalDate.now()

    private lateinit var user: UserModel

    private lateinit var calendarData: CalendarDataModel

    private val currentMonth: LocalDate = LocalDate.of(
        selectedDate.year,
        selectedDate.monthValue,
        1,
    )

    private var categoryType: CategoryType = CategoryType.EXPENSE
    private var category: String = ExpenseType.OTHERS.name

    private val _calender = MutableStateFlow<List<CalendarDateModel>>(listOf())
    private val calender = _calender.asStateFlow()

    private var _monthCashFlow = MutableStateFlow(
        MonthCashFlow(
            income = 0.0,
            expense = 0.0,
            openingAmount = 0.0,
            closingAmount = 0.0
        )
    )
    private val monthCashFlow = _monthCashFlow.asStateFlow()

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

    override suspend fun setMonthCashFLow(cashFlow: MonthCashFlow) {
        _monthCashFlow.emit(cashFlow)
    }

    override fun updateCategoryAndType(category: String, categoryType: CategoryType) {
        this.category = category
        this.categoryType = categoryType
    }

    override suspend fun updateSelectedDate(date: LocalDate) {
        selectedDate = date
    }

    override suspend fun updateCalendar(calendar: List<CalendarDateModel>) {
        _calender.emit(calendar)
    }

    override suspend fun fetchAllTransactionsOfUser(userId: Int): Map<LocalDate, List<TransactionModel>> {
        val rawTransactions = transactionDao.getAllTransactions(userId)
        return Utils.parseTransactions(rawTransactions)
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
                time = transaction.time,
                userId = user.id,
                month = transaction.month,
                year = transaction.year
            )
        )
    }

    override suspend fun removeTransaction(transaction: TransactionModel) {
        user = user.copy(transactions = deleteTransaction(transaction))
        transactionDao.removeTransaction(
            date = transaction.date,
            time = transaction.time,
            userId = user.id
        )
    }

    override fun getTransactionsByDate(date: LocalDate) = user.transactions[date]
    override fun getAllTransactions(): Map<LocalDate, List<TransactionModel>> = user.transactions
    override fun getCurrentMonth(): LocalDate = currentMonth
    override fun getCalender(): StateFlow<List<CalendarDateModel>> = calender
    override fun getUser(): UserModel = user
    override fun getCategoryType(): CategoryType = categoryType
    override fun getCategory(): String = category
    override fun getTotalExpenseOfMonth(): Double = monthCashFlow.value.expense
    override fun getTotalIncomeOfMonth(): Double = monthCashFlow.value.income
    override fun getSelectedDate(): LocalDate = selectedDate
    override fun getTransactionsOfSelectedDate() = user.transactions[selectedDate]
    override fun getMonthCashFlow(): StateFlow<MonthCashFlow> = monthCashFlow
    override fun getCashFlowOfMonth(localDate: LocalDate): MonthCashFlow = monthCashFlow.value

    override suspend fun insertCashFlowIntoDb(cashFlow: CashFlow) {
        cashFlowDao.insertCashFlow(cashFlow)
        _monthCashFlow.emit(
            MonthCashFlow(
                income = cashFlow.income,
                expense = cashFlow.expense,
                openingAmount = cashFlow.openingAmount,
                closingAmount = cashFlow.closingAmount,
            )
        )
    }

    override suspend fun fetchCashFlowOfMonth(month: Int, year: Int): MonthCashFlow {
        val cashFlow = cashFlowDao.getCashFlowOfMonth(month, year)
        return MonthCashFlow(
            income = cashFlow?.income ?: 0.0,
            expense = cashFlow?.expense ?: 0.0,
            openingAmount = cashFlow?.openingAmount ?: 0.0,
            closingAmount = cashFlow?.closingAmount ?: 0.0,
        )
    }

    override suspend fun updateMonthCashFlow(cashFlow: MonthCashFlow, isExpense: Boolean) {
        val month = selectedDate.monthValue
        val year = selectedDate.year
        if (isExpense) cashFlowDao.updateExpense(month, year, cashFlow.expense)
        else cashFlowDao.updateIncome(month, year, cashFlow.income)
        cashFlowDao.updateClosingBalance(month, year, cashFlow.closingAmount)
        _monthCashFlow.emit(cashFlow)
    }

    override suspend fun fetchAllTransactionsOfMonthAndYear(
        month: Int,
        year: Int
    ): List<TransactionModel> {
        val rawTransactions = transactionDao.getAllTransactionOfMonthAndYear(month, year, user.id)
        val transactions = mutableListOf<TransactionModel>()
        rawTransactions.forEach {
            transactions.add(Utils.convertTransactionToTransactionModel(it))
        }
        return transactions
    }

    override suspend fun fetchAllTransactionsOfTypeWithMonthAndYear(
        month: Int,
        year: Int,
        categoryType: CategoryType
    ): List<TransactionModel> {
        val rawTransactions = transactionDao.fetchAllTransactionsOfTypeWithMonthAndYear(
            month = month,
            year = year,
            userId = user.id,
            categoryType = categoryType
        )
        val transactions = mutableListOf<TransactionModel>()
        rawTransactions.forEach {
            transactions.add(Utils.convertTransactionToTransactionModel(it))
        }
        return transactions
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