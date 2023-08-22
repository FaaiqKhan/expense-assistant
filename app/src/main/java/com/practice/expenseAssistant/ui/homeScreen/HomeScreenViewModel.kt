package com.practice.expenseAssistant.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val expenseAssistantRepository: ExpenseAssistantRepository
) : ViewModel() {

    private var dates: List<CalendarDateModel> = Utils.createCalenderDays(
        todayDate = expenseAssistantRepository.getTodayDate(),
        month = expenseAssistantRepository.getCurrentMonth(),
        transactions = expenseAssistantRepository.getUser().transactions,
    )

    init {
        expenseAssistantRepository.setCalenderDates(dates)
        expenseAssistantRepository.setCalenderData(
            CalendarDataModel(
                localDate = expenseAssistantRepository.getCurrentMonth(),
                localCalendar = dates
            ),
        )
    }

    private val _calenderDates: MutableStateFlow<HomeScreenUiState> = MutableStateFlow(
        HomeScreenUiState.Success(
            balanceModel = expenseAssistantRepository.getBalance(),
            calendarData = expenseAssistantRepository.getMonthCalenderModel(),
        )
    )
    val localCalender = _calenderDates.asStateFlow()

    fun updateSelectedDate(listIndex: Int) {
        viewModelScope.launch {
            val updatedCalendar = dates.map {
                if (it.id == listIndex) {
                    expenseAssistantRepository.updateSelectedDate(it.date)
                    it.copy(isSelected = true)
                } else {
                    it.copy(isSelected = false)
                }
            }
//            expenseAssistantRepository.setCalenderDates(updatedCalendar)
            expenseAssistantRepository.updateCalendar(updatedCalendar)
            _calenderDates.emit(
                HomeScreenUiState.Success(
                    calendarData = CalendarDataModel(
                        localDate = expenseAssistantRepository.getCurrentMonth(),
                        localCalendar = updatedCalendar
                    ),
                    balanceModel = expenseAssistantRepository.getBalance()
                )
            )
        }
    }

    fun backToToday() {
        viewModelScope.launch {
            _calenderDates.emit(
                HomeScreenUiState.Success(
                    CalendarDataModel(
                        localDate = expenseAssistantRepository.getCurrentMonth(),
                        localCalendar = dates.map {
                            if (it.date == LocalDate.now()) {
                                expenseAssistantRepository.setDate(it.date)
                                it.copy(isSelected = true)
                            } else {
                                it.copy(isSelected = false)
                            }
                        }
                    ),
                    balanceModel = expenseAssistantRepository.getBalance()
                )
            )
        }
    }

    fun getUser(): UserModel = expenseAssistantRepository.getUser()
}