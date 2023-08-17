package com.practice.expenseAssistant.ui.loginScreen

sealed class LoginScreenUiState {
    object Ideal : LoginScreenUiState()
    object Success : LoginScreenUiState()
    data class Failure(val msg: String) : LoginScreenUiState()
    object Loading : LoginScreenUiState()

}
