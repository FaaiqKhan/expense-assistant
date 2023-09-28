package com.practice.expenseAssistant.ui.expensesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.utils.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ExpensesScreenViewModel @Inject constructor(
    val repository: ExpenseAssistantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExpensesScreenUiState>(ExpensesScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getAllTransactionsOfMonth(repository.getSelectedDate())
    }

    fun getAllTransactionsOfMonth(localDate: LocalDate) {
        viewModelScope.launch {
            _uiState.emit(ExpensesScreenUiState.Loading)
            val transactions = repository.fetchAllTransactionsOfTypeWithMonthAndYear(
                month = localDate.monthValue,
                year = localDate.year,
                categoryType = CategoryType.EXPENSE
            )
            _uiState.emit(ExpensesScreenUiState.Success(transactions))
        }
    }

    fun getTotalExpenses(): Double = repository.getTotalExpenseOfMonth()
}