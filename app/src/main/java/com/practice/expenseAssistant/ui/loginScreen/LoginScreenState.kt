package com.practice.expenseAssistant.ui.loginScreen

sealed class LoginScreenState {
    object Ideal : LoginScreenState()
    object Success : LoginScreenState()
    data class Failure(val msg: String) : LoginScreenState()
    object Loading : LoginScreenState()

}
