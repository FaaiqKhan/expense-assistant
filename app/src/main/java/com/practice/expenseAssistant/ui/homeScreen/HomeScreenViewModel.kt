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

    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        updateCalenderWithMonthYear(repository.getSelectedDate())
    }

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
            _uiState.emit(HomeScreenUiState.Success(updatedCalendar))
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
            _uiState.emit(HomeScreenUiState.Success(selectTodayDate))
        }
    }

    fun getUser(): UserModel = repository.getUser()

    fun getTransactionsBySelectedDate() = repository.getTransactionsOfSelectedDate() ?: listOf()

    fun getMonthCashFlow(): StateFlow<MonthCashFlow> = repository.getMonthCashFlow()

    fun getSelectedDate(): LocalDate = repository.getSelectedDate()

    fun updateCalenderWithMonthYear(date: LocalDate) {
        viewModelScope.launch {
            _uiState.emit(HomeScreenUiState.Loading)
            val data = repository.fetchAllTransactionsOfMonthAndYear(
                month = date.monthValue,
                year = date.year
            )
            val transactions = Utils.parseTransactions2(data)
            val calender = Utils.createCalenderDays(
                year = date.year,
                month = date.monthValue,
                date = date.dayOfMonth,
                transactions = transactions
            )
            repository.updateSelectedDate(date)
            repository.updateCalendar(calender)
            _uiState.emit(HomeScreenUiState.Success(calender))
        }
    }
}