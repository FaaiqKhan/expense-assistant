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
            var totalAmount = 0.0
            val dates = expenseAssistantRepository.getCalender().value.map {
                if (it.date != transaction.date) {
                    it.copy(isSelected = it.date == expenseAssistantRepository.getSelectedDate())
                } else {
                    var todayTotalIncome = it.todayTotalIncome
                    var todayTotalExpense = it.todayTotalExpense

                    if (transaction.categoryType == CategoryType.EXPENSE) {
                        totalAmount = expenseAssistantRepository
                            .getBalance().totalExpense + transaction.amount
                        todayTotalExpense += transaction.amount
                    } else {
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
            expenseAssistantRepository.setTotalExpenseOfMonth(totalAmount)
            expenseAssistantRepository.addTransaction(transaction, bankAccount)
        }
    }

    fun removeTransaction(transaction: TransactionModel) {
        viewModelScope.launch {
            var totalAmount = 0.0
            val dates = expenseAssistantRepository.getCalender().value.map {
                if (it.date != transaction.date) {
                    it.copy(isSelected = it.date == expenseAssistantRepository.getSelectedDate())
                } else {
                    var todayTotalIncome = it.todayTotalIncome
                    var todayTotalExpense = it.todayTotalExpense

                    if (transaction.categoryType == CategoryType.EXPENSE) {
                        totalAmount = expenseAssistantRepository
                            .getBalance().totalExpense - transaction.amount
                        todayTotalExpense -= transaction.amount
                    } else {
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
            expenseAssistantRepository.setTotalExpenseOfMonth(totalAmount)
        }
    }

    fun getCategoryType(): CategoryType = expenseAssistantRepository.getCategoryType()

    fun getCategory(): String = expenseAssistantRepository.getCategory()
}