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
                    var todayTotalIncome = 0.0
                    var todayTotalExpense = 0.0
                    val todayTransactions = mutableListOf<TransactionModel>()

                    if (transaction.categoryType == CategoryType.EXPENSE) {
                        totalAmount = expenseAssistantRepository
                            .getBalance().totalExpense + transaction.amount
                        todayTotalExpense = transaction.amount
                    } else {
                        todayTotalIncome = transaction.amount
                    }

                    if (it.todayTransactions != null) {
                        it.todayTransactions.forEach { item ->
                            if (item.categoryType == CategoryType.EXPENSE) {
                                todayTotalExpense += item.amount
                            } else {
                                todayTotalIncome += item.amount
                            }
                            todayTransactions.add(item)
                        }
                    }

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
                if (it.date == transaction.date) {
                    totalAmount =
                        expenseAssistantRepository.getBalance().totalExpense - transaction.amount
                    val expenses = it.todayTransactions?.toMutableList()
                    expenses?.remove(transaction)
                    it.copy(
                        isSelected = transaction.date == expenseAssistantRepository.getSelectedDate(),
                        todayTransactions = expenses?.toList()
                    )
                } else {
                    it.copy(isSelected = it.date == expenseAssistantRepository.getSelectedDate())
                }
            }
            expenseAssistantRepository.updateCalendar(dates)
            expenseAssistantRepository.setTotalExpenseOfMonth(totalAmount)
            expenseAssistantRepository.removeTransaction(transaction)
        }
    }

    fun getCategoryType(): CategoryType = expenseAssistantRepository.getCategoryType()

    fun getCategory(): String = expenseAssistantRepository.getCategory()
}