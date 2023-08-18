package com.practice.expenseAssistant.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val expenseAssistantRepository: ExpenseAssistantRepository
) : ViewModel() {

    private val toDayMonth: LocalDate = LocalDate.of(
        LocalDate.now().year,
        LocalDate.now().month,
        1
    )

    var today: LocalDate = LocalDate.now()

    private var dates = Utils.createCalenderDays(toDayMonth, today)

    private val balanceModel = BalanceModel(
        openingBalance = 2000.0,
        closingBalance = 800.0,
        totalExpense = 1200.0,
    )
    private val calendarModel = CalendarDataModel(localDate = toDayMonth, localCalendar = dates)

    private val _calenderDates: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(
            HomeScreenUiState.Success(calendarData = calendarModel, balanceModel = balanceModel)
        )
    val localCalender = _calenderDates.asStateFlow()

    var categoryType: CategoryType = CategoryType.EXPENSE
        private set
    var category: Any = ExpenseType.OTHERS
        private set

    fun updateSelectedDate(listIndex: Int) {
        viewModelScope.launch {
            _calenderDates.emit(
                HomeScreenUiState.Success(
                    calendarData = CalendarDataModel(
                        localDate = toDayMonth,
                        localCalendar = dates.map {
                            if (it.id == listIndex) {
                                today = it.date
                                it.copy(isSelected = true)
                            } else {
                                it.copy(isSelected = false)
                            }
                        }
                    ),
                    balanceModel = balanceModel
                )
            )
        }
    }

    fun backToToday() {
        viewModelScope.launch {
            _calenderDates.emit(
                HomeScreenUiState.Success(
                    CalendarDataModel(
                        localDate = toDayMonth,
                        localCalendar = dates.map {
                            if (it.date == LocalDate.now()) {
                                today = it.date
                                it.copy(isSelected = true)
                            } else {
                                it.copy(isSelected = false)
                            }
                        }
                    ),
                    balanceModel = balanceModel
                )
            )
        }
    }

    fun addTransaction(transactionModel: TransactionModel, bankAccount: BankAccount) {
        viewModelScope.launch {
            var totalAmount = 0.0
            dates = dates.map {
                if (it.date == transactionModel.date) {
                    if (transactionModel.categoryType == CategoryType.EXPENSE)
                        totalAmount = balanceModel.totalExpense + transactionModel.amount
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
            expenseAssistantRepository.addTransaction(transactionModel, bankAccount)
            _calenderDates.emit(
                HomeScreenUiState.Success(
                    calendarData = CalendarDataModel(localDate = toDayMonth, localCalendar = dates),
                    balanceModel = balanceModel.copy(totalExpense = totalAmount)
                )
            )
        }
    }

    fun removeExpense(transactionModel: TransactionModel) {
        viewModelScope.launch {
            var totalAmount = 0.0
            dates = dates.map {
                if (it.date == transactionModel.date) {
                    totalAmount = balanceModel.totalExpense - transactionModel.amount
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
            _calenderDates.emit(
                HomeScreenUiState.Success(
                    calendarData = CalendarDataModel(localDate = toDayMonth, localCalendar = dates),
                    balanceModel = balanceModel.copy(totalExpense = totalAmount)
                )
            )
        }
    }

    fun updateCategory(type: CategoryType, category: Any) {
        categoryType = type
        this.category = category
    }

    fun updateBankAccount(bankAccount: BankAccount) {
//        this.bankAccount = bankAccount
    }

    fun getUser(): UserModel = expenseAssistantRepository.getUser()
}