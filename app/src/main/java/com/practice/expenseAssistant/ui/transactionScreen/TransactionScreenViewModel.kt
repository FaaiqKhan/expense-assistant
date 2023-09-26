package com.practice.expenseAssistant.ui.transactionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.data.TransactionModel
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
                    if (transaction.categoryType == CategoryType.EXPENSE) {
                        val cashFlowState = expenseAssistantRepository.getMonthCashFlow().value
                        val totalAmount = cashFlowState.expense + transaction.amount
                        val closingAmountDelta = cashFlowState.closingAmount - transaction.amount
                        val cashFlow = cashFlowState.copy(
                            expense = totalAmount,
                            closingAmount = closingAmountDelta,
                        )
                        expenseAssistantRepository.updateMonthCashFlow(cashFlow, true)
                        todayTotalExpense += transaction.amount
                        expenseAssistantRepository.setTotalExpenseOfMonth(totalAmount)
                    } else {
                        val cashFlowState = expenseAssistantRepository.getMonthCashFlow().value
                        val totalAmount = cashFlowState.income + transaction.amount
                        val closingAmountDiff = cashFlowState.closingAmount + transaction.amount
                        val cashFlow = cashFlowState.copy(
                            income = totalAmount,
                            closingAmount = closingAmountDiff
                        )
                        expenseAssistantRepository.updateMonthCashFlow(cashFlow, false)
                        todayTotalIncome += transaction.amount
                    }

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
                        val cashFlowState = expenseAssistantRepository.getMonthCashFlow().value
                        val totalAmount = cashFlowState.expense - transaction.amount
                        val closingAmountDiff = cashFlowState.closingAmount + transaction.amount
                        val cashFlow = cashFlowState.copy(
                            expense = totalAmount,
                            closingAmount = closingAmountDiff
                        )
                        expenseAssistantRepository.updateMonthCashFlow(cashFlow, isExpense = true)
                        expenseAssistantRepository.setTotalExpenseOfMonth(totalAmount)
                        todayTotalExpense -= transaction.amount
                    } else {
                        val cashFlowState = expenseAssistantRepository.getMonthCashFlow().value
                        val totalAmount = cashFlowState.income - transaction.amount
                        val closingAmountDelta = cashFlowState.closingAmount - transaction.amount
                        val cashFlow = cashFlowState.copy(
                            income = totalAmount,
                            closingAmount = closingAmountDelta
                        )
                        expenseAssistantRepository.updateMonthCashFlow(cashFlow, isExpense = false)
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