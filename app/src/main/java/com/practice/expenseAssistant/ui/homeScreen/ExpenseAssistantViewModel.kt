package com.practice.expenseAssistant.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.utils.*
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

    val backAccounts = mapOf("HBL" to "123234235134", "Meezan" to "50387395032")
    val currencyType = Utils.currencyIcons.getValue("Euro")

    val calendarOfMonth: LocalDate = LocalDate.of(
        LocalDate.now().year,
        LocalDate.now().month,
        1
    )

    var today: LocalDate = LocalDate.now()

    private var dates = Utils.createCalenderDays(calendarOfMonth, today)

    private val _calenderDates: MutableStateFlow<List<CalendarDateModel>> = MutableStateFlow(dates)
    val calenderDates: StateFlow<List<CalendarDateModel>> = _calenderDates

    val monthOpeningBalance: Int = 45000
    val monthClosingBalance: Int = 20000

    private var totalExpenseOfMonth: Int = 0

    private val _totalExpenseOfMonth: MutableStateFlow<Int> = MutableStateFlow(totalExpenseOfMonth)
    val totalExpenseOfMonthState: StateFlow<Int> = _totalExpenseOfMonth

    var categoryType: CategoryType = CategoryType.EXPENSE
        private set
    var category: Any = ExpenseType.OTHERS
        private set

    fun updateSelectedDate(listIndex: Int) {
        viewModelScope.launch {
            _calenderDates.emit(
                dates.map {
                    if (it.id == listIndex) {
                        today = it.date
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
                        today = it.date
                        it.copy(isSelected = true)
                    } else {
                        it.copy(isSelected = false)
                    }
                }
            )
        }
    }

    fun addExpense(expenseModel: ExpenseModel) {
        viewModelScope.launch {
            dates = dates.map {
                if (it.date == expenseModel.date) {
                    totalExpenseOfMonth += expenseModel.expense
                    val expenses = it.expenseModel.toMutableList()
                    expenses.add(expenseModel)
                    it.copy(
                        isSelected = expenseModel.date == today,
                        expenseModel = expenses.toList()
                    )
                } else {
                    it.copy(isSelected = it.date == today)
                }
            }
            _calenderDates.emit(dates)
            _totalExpenseOfMonth.emit(totalExpenseOfMonth)
        }
    }

    fun removeExpense(selectedDate: LocalDate, expenseModel: ExpenseModel) {
        viewModelScope.launch {
            dates = dates.map {
                if (it.date == expenseModel.date) {
                    totalExpenseOfMonth -= expenseModel.expense
                    val expenses = it.expenseModel.toMutableList()
                    expenses.remove(expenseModel)
                    it.copy(
                        isSelected = expenseModel.date == today,
                        expenseModel = expenses.toList()
                    )
                } else {
                    it.copy(isSelected = it.date == today)
                }
            }
            _calenderDates.emit(dates)
            _totalExpenseOfMonth.emit(totalExpenseOfMonth)
        }
    }

    fun updateCategory(type: CategoryType, category: Any) {
        categoryType = type
        this.category = category
    }
}