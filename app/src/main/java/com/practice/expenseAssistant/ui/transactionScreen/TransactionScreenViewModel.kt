package com.practice.expenseAssistant.ui.transactionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.utils.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionScreenViewModel @Inject constructor(
    private val expenseAssistantRepository: ExpenseAssistantRepository
) : ViewModel() {

    fun getUser() = expenseAssistantRepository.getUser()
    fun getSelectedDate() = expenseAssistantRepository.getSelectedDate()

    fun addTransaction(transaction: TransactionModel, bankAccount: BankAccount) {
        viewModelScope.launch {
            val dates = expenseAssistantRepository.getCalender().value.map {
                if (it.date != transaction.date) {
                    it.copy(isSelected = it.date == expenseAssistantRepository.getSelectedDate())
                } else {
                    val data = processTransaction(
                        calendarDateModel = it,
                        transaction = transaction,
                        monthCashFlow = expenseAssistantRepository.getMonthCashFlow().value,
                        processToRemove = false,
                    )

                    val totalAmount = data[0] as Double
                    val todayTotalIncome = data[1] as Double
                    val todayTotalExpense = data[2] as Double
                    val monthUpdatedCashFlow = data[3] as MonthCashFlow

                    if (transaction.categoryType == CategoryType.INCOME) {
                        expenseAssistantRepository.setTotalExpenseOfMonth(totalAmount)
                    }

                    expenseAssistantRepository.updateMonthCashFlow(
                        cashFlow = monthUpdatedCashFlow,
                        isExpense = transaction.categoryType == CategoryType.EXPENSE
                    )

                    val todayTransactions = it.todayTransactions?.toMutableList() ?: mutableListOf()
                    todayTransactions.add(transaction)

                    it.copy(
                        isSelected = transaction.date == expenseAssistantRepository.getSelectedDate(),
                        todayTransactions = todayTransactions,
                        todayTotalIncome = todayTotalIncome,
                        todayTotalExpense = todayTotalExpense
                    )
                }
            }

            expenseAssistantRepository.updateCalendar(dates)
            expenseAssistantRepository.addTransaction(transaction, bankAccount)
        }
    }

    fun removeTransaction(transaction: TransactionModel) {
        viewModelScope.launch {
            val dates = expenseAssistantRepository.getCalender().value.map {
                if (it.date != transaction.date) {
                    it.copy(isSelected = it.date == expenseAssistantRepository.getSelectedDate())
                } else {
                    val data = processTransaction(
                        calendarDateModel = it,
                        transaction = transaction,
                        monthCashFlow = expenseAssistantRepository.getMonthCashFlow().value,
                        processToRemove = true,
                    )

                    val totalAmount = data[0] as Double
                    val todayTotalIncome = data[1] as Double
                    val todayTotalExpense = data[2] as Double
                    val monthUpdatedCashFlow = data[3] as MonthCashFlow

                    if (transaction.categoryType == CategoryType.INCOME) {
                        expenseAssistantRepository.setTotalExpenseOfMonth(totalAmount)
                    }

                    expenseAssistantRepository.updateMonthCashFlow(
                        cashFlow = monthUpdatedCashFlow,
                        isExpense = transaction.categoryType == CategoryType.EXPENSE
                    )

                    val todayTransactions = it.todayTransactions!!.toMutableList()
                    todayTransactions.remove(transaction)

                    it.copy(
                        isSelected = transaction.date == expenseAssistantRepository.getSelectedDate(),
                        todayTransactions = todayTransactions,
                        todayTotalIncome = todayTotalIncome,
                        todayTotalExpense = todayTotalExpense
                    )
                }
            }
            expenseAssistantRepository.updateCalendar(dates)
            expenseAssistantRepository.removeTransaction(transaction)
        }
    }

    fun getCategoryType(): CategoryType = expenseAssistantRepository.getCategoryType()

    fun getCategory(): String = expenseAssistantRepository.getCategory()

    private fun processTransaction(
        calendarDateModel: CalendarDateModel,
        transaction: TransactionModel,
        monthCashFlow: MonthCashFlow,
        processToRemove: Boolean
    ): List<Any> {
        val totalAmount: Double
        val monthUpdatedCashFlow: MonthCashFlow
        var closingAmount = monthCashFlow.closingAmount
        var todayTotalIncome = calendarDateModel.todayTotalIncome
        var todayTotalExpense = calendarDateModel.todayTotalExpense
        if (transaction.categoryType == CategoryType.INCOME) {
            totalAmount = if (processToRemove) {
                closingAmount -= transaction.amount
                todayTotalIncome -= transaction.amount
                monthCashFlow.income - transaction.amount
            } else {
                closingAmount += transaction.amount
                todayTotalIncome += transaction.amount
                monthCashFlow.income + transaction.amount
            }
            monthUpdatedCashFlow = monthCashFlow.copy(
                income = totalAmount,
                closingAmount = closingAmount
            )
        } else {
            totalAmount = if (processToRemove) {
                closingAmount += transaction.amount
                todayTotalExpense -= transaction.amount
                monthCashFlow.expense - transaction.amount
            } else {
                closingAmount -= transaction.amount
                todayTotalExpense += transaction.amount
                monthCashFlow.expense + transaction.amount
            }
            monthUpdatedCashFlow = monthCashFlow.copy(
                expense = totalAmount,
                closingAmount = closingAmount
            )
        }
        return listOf(totalAmount, todayTotalIncome, todayTotalExpense, monthUpdatedCashFlow)
    }
}