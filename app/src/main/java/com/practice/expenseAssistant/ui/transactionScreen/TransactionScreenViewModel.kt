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
                        val totalAmount = expenseAssistantRepository.getMonthCashFlow()
                            .value.expense + transaction.amount
                        val closingAmountDelta = expenseAssistantRepository.getMonthCashFlow()
                            .value.closingAmount - transaction.amount
                        expenseAssistantRepository.updateMonthCashFlow(
                            expenseAssistantRepository.getMonthCashFlow().value.copy(
                                expense = totalAmount,
                                closingAmount = closingAmountDelta,
                            )
                        )
                        todayTotalExpense += transaction.amount
                        expenseAssistantRepository.setTotalExpenseOfMonth(totalAmount)
                    } else {
                        val totalAmount = expenseAssistantRepository.getMonthCashFlow()
                            .value.income + transaction.amount
                        val closingAmountDiff = expenseAssistantRepository.getMonthCashFlow()
                            .value.closingAmount + transaction.amount
                        expenseAssistantRepository.updateMonthCashFlow(
                            expenseAssistantRepository.getMonthCashFlow().value.copy(
                                income = totalAmount,
                                closingAmount = closingAmountDiff
                            )
                        )
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
                        val totalAmount = expenseAssistantRepository.getMonthCashFlow()
                            .value.expense - transaction.amount
                        val closingAmountDiff = expenseAssistantRepository.getMonthCashFlow()
                            .value.closingAmount + transaction.amount
                        expenseAssistantRepository.updateMonthCashFlow(
                            expenseAssistantRepository.getMonthCashFlow().value.copy(
                                expense = totalAmount,
                                closingAmount = closingAmountDiff
                            )
                        )
                        expenseAssistantRepository.setTotalExpenseOfMonth(totalAmount)
                        todayTotalExpense -= transaction.amount
                    } else {
                        val totalAmount = expenseAssistantRepository.getMonthCashFlow()
                            .value.income - transaction.amount
                        val closingAmountDelta = expenseAssistantRepository.getMonthCashFlow()
                            .value.closingAmount - transaction.amount
                        expenseAssistantRepository.updateMonthCashFlow(
                            expenseAssistantRepository.getMonthCashFlow().value.copy(
                                income = totalAmount,
                                closingAmount = closingAmountDelta
                            )
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