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
                    var todayTotalIncome = it.todayTotalIncome
                    var todayTotalExpense = it.todayTotalExpense
                    val monthCashFlow = expenseAssistantRepository.getCashFlowOfMonth(
                        transaction.date
                    )
                    val updatedMonthCashFlow: MonthCashFlow
                    if (transaction.categoryType == CategoryType.EXPENSE) {
                        val totalExpense = monthCashFlow.expense + transaction.amount
                        todayTotalExpense += transaction.amount
                        updatedMonthCashFlow = monthCashFlow.copy(
                            expense = totalExpense,
                            closingAmount = monthCashFlow.closingAmount - transaction.amount
                        )
                        expenseAssistantRepository.setTotalExpenseOfMonth(totalExpense)
                    } else {
                        val totalIncome = monthCashFlow.income + transaction.amount
                        todayTotalIncome += transaction.amount
                        updatedMonthCashFlow = monthCashFlow.copy(
                            income = totalIncome,
                            closingAmount = monthCashFlow.closingAmount - transaction.amount
                        )
                        expenseAssistantRepository.setTotalIncomeOfMonth(totalIncome)
                    }
                    expenseAssistantRepository.updateMonthCashFlow(
                        cashFlow = updatedMonthCashFlow,
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
                    var todayTotalIncome = it.todayTotalIncome
                    var todayTotalExpense = it.todayTotalExpense

                    if (transaction.categoryType == CategoryType.EXPENSE) {
                        val cashFlow =
                            expenseAssistantRepository.getCashFlowOfMonth(transaction.date)
                        val totalAmount = cashFlow.expense - transaction.amount
                        val closingAmountDiff = cashFlow.closingAmount + transaction.amount
                        val updatedCashFlow = cashFlow.copy(
                            expense = totalAmount,
                            closingAmount = closingAmountDiff
                        )
                        expenseAssistantRepository.updateMonthCashFlow(
                            updatedCashFlow,
                            isExpense = true
                        )
                        expenseAssistantRepository.setTotalExpenseOfMonth(totalAmount)
                        todayTotalExpense -= transaction.amount
                    } else {
                        val cashFlow =
                            expenseAssistantRepository.getCashFlowOfMonth(transaction.date)
                        val totalAmount = cashFlow.income - transaction.amount
                        val closingAmountDelta = cashFlow.closingAmount - transaction.amount
                        val updatedCashFlow = cashFlow.copy(
                            income = totalAmount,
                            closingAmount = closingAmountDelta
                        )
                        expenseAssistantRepository.updateMonthCashFlow(
                            updatedCashFlow,
                            isExpense = false
                        )
                        todayTotalIncome -= transaction.amount
                    }

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
}