package com.practice.expenseAssistant.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: ExpenseAssistantRepository
) : ViewModel() {

    private var currentMonth = repository.getCurrentMonth()

    fun updateSelectedDate(listIndex: Int) {
        viewModelScope.launch {
            val updatedCalendar = repository.getCalender().value.map {
                if (it.id == listIndex) {
                    repository.updateSelectedDate(it.date)
                    it.copy(isSelected = true)
                } else {
                    it.copy(isSelected = false)
                }
            }
            repository.updateCalendar(updatedCalendar)
        }
    }

    fun backToToday() {
        viewModelScope.launch {
            val selectTodayDate = repository.getCalender().value.map {
                if (it.date == LocalDate.now()) {
                    repository.updateSelectedDate(it.date)
                    it.copy(isSelected = true)
                } else {
                    it.copy(isSelected = false)
                }
            }
            repository.updateCalendar(selectTodayDate)
        }
    }

    fun getUser(): UserModel = repository.getUser()

    fun getCalender() = repository.getCalender()

    fun getTransactionsBySelectedDate() = repository.getTransactionsOfSelectedDate() ?: listOf()

    fun getMonthCashFlow(): StateFlow<MonthCashFlow> = repository.getMonthCashFlow()

    fun getCurrentMonth(): LocalDate = currentMonth

    fun updateCalenderOfMonthYear(date: LocalDate) {
        viewModelScope.launch {
            val calender = Utils.createCalenderDays(
                year = date.year,
                month = date.monthValue,
                date = date.dayOfMonth,
                transactions = Utils.parseTransactions2(
                    repository.fetchAllTransactionsOfMonthAndYear(
                        month = date.monthValue,
                        year = date.year
                    )
                )
            )
            currentMonth = date
            repository.updateCalendar(calender)
        }
    }
}