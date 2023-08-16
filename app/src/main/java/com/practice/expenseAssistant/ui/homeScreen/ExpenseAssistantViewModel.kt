package com.practice.expenseAssistant.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ExpenseAssistantViewModel @Inject constructor(
    private val expenseAssistantRepository: ExpenseAssistantRepository
) : ViewModel() {

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

    var bankAccount: BankAccount = BankAccount(
        iBan = "MZN000813415322408213",
        name = "Mezzanine Bank Limited",
        number = "00200395038219",
        balance = 160000.00
    )

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

    fun addTransaction(transactionModel: TransactionModel) {
        viewModelScope.launch {
            dates = dates.map {
                if (it.date == transactionModel.date) {
                    if (transactionModel.categoryType == CategoryType.EXPENSE)
                        totalExpenseOfMonth += transactionModel.amount
                    val expenses = it.transactionModel.toMutableList()
                    expenses.add(transactionModel)
                    it.copy(
                        isSelected = transactionModel.date == today,
                        transactionModel = expenses.toList()
                    )
                } else {
                    it.copy(isSelected = it.date == today)
                }
            }
            _calenderDates.emit(dates)
            _totalExpenseOfMonth.emit(totalExpenseOfMonth)
        }
    }

    fun removeExpense(transactionModel: TransactionModel) {
        viewModelScope.launch {
            dates = dates.map {
                if (it.date == transactionModel.date) {
                    totalExpenseOfMonth -= transactionModel.amount
                    val expenses = it.transactionModel.toMutableList()
                    expenses.remove(transactionModel)
                    it.copy(
                        isSelected = transactionModel.date == today,
                        transactionModel = expenses.toList()
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

    fun updateBankAccount(bankAccount: BankAccount) {
        this.bankAccount = bankAccount
    }

    fun getUser(): UserModel = expenseAssistantRepository.getUser()
}