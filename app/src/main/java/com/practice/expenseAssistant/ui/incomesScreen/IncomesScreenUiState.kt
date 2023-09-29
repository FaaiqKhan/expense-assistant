package com.practice.expenseAssistant.ui.incomesScreen

import com.practice.expenseAssistant.data.TransactionModel
import java.time.LocalDate

sealed class IncomesScreenUiState {
    object Loading : IncomesScreenUiState()
    data class Failure(val message: String) : IncomesScreenUiState()
    data class Success(
        val transactions: List<TransactionModel>,
        val date: LocalDate,
        val totalIncome: Double,
    ) : IncomesScreenUiState()
}
