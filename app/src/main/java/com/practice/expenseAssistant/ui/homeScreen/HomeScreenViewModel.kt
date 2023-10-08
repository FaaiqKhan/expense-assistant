package com.practice.expenseAssistant.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.MonthCashFlow
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.utils.CategoryType
import com.practice.expenseAssistant.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    fun getMonthCashFlow() = repository.getMonthCashFlowAsState()
    fun getCalender() = repository.getCalender()
    fun getUserName() = repository.getUser().name

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
            _uiState.emit(HomeScreenUiState.Success(repository.getUser()))
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
//            _uiState.emit(HomeScreenUiState.Success(selectTodayDate))
        }
    }

    fun getTransactionsBySelectedDate() = repository.getTransactionsOfSelectedDate() ?: listOf()

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
            val monthCashFlow = calculateMonthCashFlow(date, data)
            repository.updateSelectedDate(date)
            repository.updateCalendar(calender)
            repository.setMonthCashFLow(monthCashFlow)
            delay(timeMillis = 500L)
            _uiState.emit(HomeScreenUiState.Success(repository.getUser()))
        }
    }

    private suspend fun calculateMonthCashFlow(
        date: LocalDate,
        transactions: List<TransactionModel>
    ): MonthCashFlow {
        val prevMonthCashFlow = repository.fetchCashFlowOfMonth(
            month = date.minusMonths(1).monthValue,
            year = date.minusMonths(1).year
        )
        val monthCashFlow = repository.fetchCashFlowOfMonth(
            month = date.monthValue,
            year = date.year,
        )
        if (prevMonthCashFlow.openingAmount != 0.0) {
            var closingAmount = prevMonthCashFlow.closingAmount
            transactions.forEach {
                if (it.categoryType == CategoryType.INCOME) closingAmount += it.amount
                else closingAmount -= it.amount
            }
            return monthCashFlow.copy(
                openingAmount = prevMonthCashFlow.closingAmount,
                closingAmount = closingAmount,
            )
        }
        return monthCashFlow
    }
}