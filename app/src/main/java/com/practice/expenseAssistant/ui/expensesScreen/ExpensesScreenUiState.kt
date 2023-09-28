package com.practice.expenseAssistant.ui.expensesScreen

import com.practice.expenseAssistant.data.TransactionModel

sealed class ExpensesScreenUiState {
    object Loading : ExpensesScreenUiState()
    data class Failure(val message: String) : ExpensesScreenUiState()
    data class Success(val transactions: List<TransactionModel>) : ExpensesScreenUiState()
}