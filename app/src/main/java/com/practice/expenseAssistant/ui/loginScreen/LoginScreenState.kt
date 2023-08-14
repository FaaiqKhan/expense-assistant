package com.practice.expenseAssistant.ui.loginScreen

import com.practice.expenseAssistant.repository.database.entities.User

sealed class LoginScreenState {
    object Ideal : LoginScreenState()
    data class Success(val user: User) : LoginScreenState()
    data class Failure(val msg: String) : LoginScreenState()
    object Loading : LoginScreenState()

}
