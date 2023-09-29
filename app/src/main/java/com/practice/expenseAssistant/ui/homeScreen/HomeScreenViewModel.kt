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
    val uiState = _uiState.asStateFlow()

    private var currentMonth = repository.getCurrentMonth()

    init {
        _uiState.value = (HomeScreenUiState.Success(repository.getCalender().value))
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

    fun getCalender() = repository.getCalender()

    fun getTransactionsBySelectedDate() = repository.getTransactionsOfSelectedDate() ?: listOf()

    fun getMonthCashFlow(): StateFlow<MonthCashFlow> = repository.getMonthCashFlow()

    fun getCurrentMonth(): LocalDate = currentMonth

    fun updateCalenderOfMonthYear(date: LocalDate) {
        viewModelScope.launch {
            _uiState.emit(HomeScreenUiState.Loading)
            val calender = Utils.createCalenderDays(
                month = date,
                todayDate = date,
                transactions = Utils.parseTransactions2(
                    repository.fetchAllTransactionsOfMonthAndYear(
                        month = date.monthValue,
                        year = date.year
                    )
                )
            )
            currentMonth = date
            _uiState.emit(HomeScreenUiState.Success(calendar = calender))
        }
    }
}