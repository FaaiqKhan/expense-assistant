package com.practice.expenseAssistant.ui.homeScreen

import com.practice.expenseAssistant.data.UserModel

sealed class HomeScreenUiState {
    object Loading : HomeScreenUiState()
    data class Failure(val message: String) : HomeScreenUiState()
    data class Success(val user: UserModel) : HomeScreenUiState()
}
