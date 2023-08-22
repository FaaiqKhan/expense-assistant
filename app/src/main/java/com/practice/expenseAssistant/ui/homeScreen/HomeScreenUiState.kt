package com.practice.expenseAssistant.ui.homeScreen

import com.practice.expenseAssistant.data.BalanceModel
import com.practice.expenseAssistant.data.CalendarDataModel

sealed class HomeScreenUiState {
    object Loading : HomeScreenUiState()
    data class Failure(val message: String) : HomeScreenUiState()
    data class Success(
        val calendarData: CalendarDataModel,
        val balanceModel: BalanceModel,
    ) : HomeScreenUiState()
}
