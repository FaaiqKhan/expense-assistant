package com.practice.expenseAssistant.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ExpenseAssistantViewModel : ViewModel() {

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

    var selectedDate: LocalDate = LocalDate.now()
        private set

    private var dates = Utils.createCalenderDays(calendarOfMonth, selectedDate)

    private val _calenderDates: MutableStateFlow<List<CalendarDateModel>> = MutableStateFlow(dates)
    val calenderDates: StateFlow<List<CalendarDateModel>> = _calenderDates

    val monthOpeningBalance: Int = 45000
    val monthClosingBalance: Int = 20000

    private var totalExpenseOfMonth: Int = 0

    private val _totalExpenseOfMonth: MutableStateFlow<Int> = MutableStateFlow(totalExpenseOfMonth)
    val totalExpenseOfMonthState: StateFlow<Int> = _totalExpenseOfMonth

    fun updateSelectedDate(listIndex: Int) {
        viewModelScope.launch {
            _calenderDates.emit(
                dates.map {
                    if (it.id == listIndex) {
                        selectedDate = it.date
                        it.copy(isSelected = true)
                    } else {
                        it.copy(isSelected = false)
                    }
                }
            )
        }
    }

    fun backToToday() {
        viewModelScope.launch {
            _calenderDates.emit(
                dates.map {
                    if (it.date == LocalDate.now()) {
                        selectedDate = it.date
                        it.copy(isSelected = true)
                    } else {
                        it.copy(isSelected = false)
                    }
                }
            )
        }
    }

    fun addExpense(selectedDate: LocalDate, expenseModel: ExpenseModel) {
        viewModelScope.launch {
            dates = dates.map {
                if (it.date == selectedDate) {
                    totalExpenseOfMonth += expenseModel.expense
                    it.copy(isSelected = true, expenseModel = expenseModel)
                } else {
                    it.copy(isSelected = false)
                }
            }
            _calenderDates.emit(dates)
            _totalExpenseOfMonth.emit(totalExpenseOfMonth)
        }
    }

    fun removeExpense(selectedDate: LocalDate, expenseModel: ExpenseModel) {
        viewModelScope.launch {
            dates = dates.map {
                if (it.date == selectedDate) {
                    totalExpenseOfMonth -= expenseModel.expense
                    it.copy(isSelected = true, expenseModel = expenseModel)
                } else {
                    it.copy(isSelected = false)
                }
            }
            _calenderDates.emit(dates)
            _totalExpenseOfMonth.emit(totalExpenseOfMonth)
        }
    }
}