package com.practice.expenseAssistant.ui.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.repository.database.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {

    private val _loginScreenViewState = MutableStateFlow<LoginScreenState>(LoginScreenState.Ideal)

    val loginScreenState: StateFlow<LoginScreenState> = _loginScreenViewState

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            if (userName.isEmpty() || password.isEmpty())
                _loginScreenViewState.emit(LoginScreenState.Failure("Field cannot be empty"))
            _loginScreenViewState.emit(LoginScreenState.Loading)
            delay(timeMillis = 500L)
            _loginScreenViewState.emit(
                LoginScreenState.Success(
                    User(
                        id = 1,
                        name = "Faaiq Ali Khan",
                        bankAccount = listOf()
                    )
                )
            )
        }
    }
}
