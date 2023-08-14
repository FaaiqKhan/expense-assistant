package com.practice.expenseAssistant.ui.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.repository.database.dao.UserDao
import com.practice.expenseAssistant.repository.database.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {

    private val _loginScreenViewState = MutableStateFlow<LoginScreenState>(LoginScreenState.Ideal)

    val loginScreenState: StateFlow<LoginScreenState> = _loginScreenViewState

    fun signIn(userName: String, password: String) {
        viewModelScope.launch {
            if (userName.isEmpty() || password.isEmpty())
                _loginScreenViewState.emit(LoginScreenState.Failure("Field cannot be empty"))
            _loginScreenViewState.emit(LoginScreenState.Loading)
            delay(timeMillis = 500L)
            val user = userDao.getUser(userName, password)

        }
    }

    fun signUp(userName: String, password: String, bankAccounts: List<BankAccount>) {
        viewModelScope.launch {
            if (userName.isBlank() || password.isBlank())
                _loginScreenViewState.emit(LoginScreenState.Failure("Field cannot be empty"))
            if (bankAccounts.isEmpty())
                _loginScreenViewState.emit(LoginScreenState.Failure("Please add bank account"))
            _loginScreenViewState.emit(LoginScreenState.Loading)
            delay(timeMillis = 500L)
            userDao.setUser(
                User(
                    name = userName,
                    password = password,
                    bankAccount = bankAccounts
                )
            )
        }
    }
}
