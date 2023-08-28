package com.practice.expenseAssistant.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val expenseAssistantRepository: ExpenseAssistantRepository
) : ViewModel() {

    private val _calenderDates: MutableStateFlow<HomeScreenUiState> = MutableStateFlow(
        HomeScreenUiState.Success(
            balanceModel = expenseAssistantRepository.getBalance(),
            calendarData = expenseAssistantRepository.getMonthCalenderModel(),
        )
    )
    val localCalender = _calenderDates.asStateFlow()

    fun updateSelectedDate(listIndex: Int) {
        viewModelScope.launch {
            val updatedCalendar = expenseAssistantRepository.getCalender().value.map {
                if (it.id == listIndex) {
                    expenseAssistantRepository.updateSelectedDate(it.date)
                    it.copy(isSelected = true)
                } else {
                    it.copy(isSelected = false)
                }
            }
            expenseAssistantRepository.updateCalendar(updatedCalendar)
        }
    }

    fun backToToday() {
        viewModelScope.launch {
            val selectTodayDate = expenseAssistantRepository.getCalender().value.map {
                if (it.date == LocalDate.now()) {
                    expenseAssistantRepository.updateSelectedDate(it.date)
                    it.copy(isSelected = true)
                } else {
                    it.copy(isSelected = false)
                }
            }
            expenseAssistantRepository.updateCalendar(selectTodayDate)
        }
    }

    fun getUser(): UserModel = expenseAssistantRepository.getUser()

    fun getCalender() = expenseAssistantRepository.getCalender()

    fun getTransactionsBySelectedDate(): List<TransactionModel> {
        return expenseAssistantRepository.getTransactionsOfSelectedDate() ?: listOf()
    }
}