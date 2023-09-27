package com.practice.expenseAssistant.ui.statementScreen

import com.practice.expenseAssistant.data.TransactionModel
import java.time.LocalDate

sealed class StatementScreenUiState {
    object Loading : StatementScreenUiState()
    data class Failure(val message: String) : StatementScreenUiState()
    data class Success(val transactions: List<TransactionModel>, val date: LocalDate) :
        StatementScreenUiState()
}