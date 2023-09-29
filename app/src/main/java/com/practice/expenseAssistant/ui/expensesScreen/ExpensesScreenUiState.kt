package com.practice.expenseAssistant.ui.expensesScreen

import com.practice.expenseAssistant.data.TransactionModel
import java.time.LocalDate

sealed class ExpensesScreenUiState {
    object Loading : ExpensesScreenUiState()
    data class Failure(val message: String) : ExpensesScreenUiState()
    data class Success(
        val transactions: List<TransactionModel>,
        val date: LocalDate,
        val totalExpense: Double,
    ) : ExpensesScreenUiState()
}