package com.practice.expenseAssistant.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeScreenViewModel : ViewModel() {

    val userModel = UserModel(
        name = "FaaiqKhan",
        accounts = listOf(
            BankAccountModel(
                accountNumber = 123324,
                accountName = "MEEZAN",
                accountBalance = 123.4
            )
        )
    )

    val calendarOfMonth: LocalDate = LocalDate.of(
        LocalDate.now().year,
        LocalDate.now().month,
        1
    )

    private val dates = Utils.createCalenderDays(calendarOfMonth, LocalDate.now())

    private val _calenderDates: MutableStateFlow<List<CalendarDateModel>> = MutableStateFlow(dates)

    val calenderDates: StateFlow<List<CalendarDateModel>> = _calenderDates

    var selectedDate: LocalDate = LocalDate.now()
        private set

    fun updateSelectedDate(listIndex: Int) {
        viewModelScope.launch {
            _calenderDates.emit(
                dates.map { item ->
                    if (item.id == listIndex) {
                        item.copy(isSelected = true)
                    } else {
                        item.copy(isSelected = false)
                    }
                }
            )
        }
    }
}