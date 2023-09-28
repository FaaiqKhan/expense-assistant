package com.practice.expenseAssistant.ui.statementScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatementScreenViewModel @Inject constructor(
    private val repository: ExpenseAssistantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatementScreenUiState>(StatementScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getAllTransactionsOfMonth(repository.getCurrentMonth())
    }

    fun getAllTransactionsOfMonth(date: LocalDate) {
        viewModelScope.launch {
            val data = repository.fetchAllTransactionsOfMonthAndYear(
                date.monthValue,
                date.year,
            )
            val transactions = mutableListOf<TransactionModel>()
            data.forEach {
                transactions.add(
                    TransactionModel(
                        categoryType = it.categoryType,
                        category = it.category,
                        note = it.note,
                        amount = it.amount,
                        date = it.date,
                        time = it.time,
                        month = it.month,
                        year = it.year
                    )
                )
            }
            _uiState.emit(StatementScreenUiState.Success(transactions, date))
        }
    }
}