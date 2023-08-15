package com.practice.expenseAssistant.ui.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.repository.database.dao.UserDao
import com.practice.expenseAssistant.repository.database.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {

    private val _loginScreenViewState = MutableStateFlow<LoginScreenState>(LoginScreenState.Ideal)

    val loginScreenState: StateFlow<LoginScreenState> = _loginScreenViewState

    fun signIn(
        userName: String,
        password: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler of signIn got $exception")
        }
        viewModelScope.launch(handler) {
            withContext(dispatcher) {
                if (userName.isBlank() || password.isBlank()) {
                    _loginScreenViewState.emit(LoginScreenState.Failure("Field cannot be empty"))
                    return@withContext
                }
                _loginScreenViewState.emit(LoginScreenState.Loading)
                delay(timeMillis = 500L)
                val user: User? = userDao.getUser(userName, password)
                if (user == null) {
                    _loginScreenViewState.emit(LoginScreenState.Failure("Invalid username of password"))
                    return@withContext
                }
                _loginScreenViewState.emit(LoginScreenState.Success(user))
            }
        }
    }

    fun signUp(
        userName: String,
        password: String,
        bankAccounts: List<BankAccount>,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler of signUp got $exception")
        }
        viewModelScope.launch(handler) {
            withContext(dispatcher) {
                if (userName.isBlank() || password.isBlank()) {
                    _loginScreenViewState.emit(LoginScreenState.Failure("User name & password cannot be empty"))
                    return@withContext
                }
                if (bankAccounts.isEmpty()) {
                    _loginScreenViewState.emit(LoginScreenState.Failure("Please add bank account"))
                    return@withContext
                }
                _loginScreenViewState.emit(LoginScreenState.Loading)
                delay(timeMillis = 500L)
                val user = User(
                    name = userName,
                    password = password,
                    bankAccount = bankAccounts
                )
                userDao.setUser(user)
                _loginScreenViewState.emit(LoginScreenState.Success(user))
            }
        }
    }
}
