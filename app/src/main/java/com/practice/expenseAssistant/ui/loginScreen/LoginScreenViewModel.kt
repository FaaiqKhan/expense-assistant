package com.practice.expenseAssistant.ui.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.data.UserModel
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.repository.database.dao.UserDao
import com.practice.expenseAssistant.repository.database.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val userDao: UserDao,
    private val expenseAssistantRepository: ExpenseAssistantRepository,
) : ViewModel() {

    private val _loginScreenViewState = MutableStateFlow<LoginScreenUiState>(LoginScreenUiState.Ideal)

    val loginScreenUiState: StateFlow<LoginScreenUiState> = _loginScreenViewState

    fun signIn(
        userName: String,
        password: String,
    ) {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler of signIn got $exception")
            return@CoroutineExceptionHandler
        }
        viewModelScope.launch(handler) {
            if (userName.isBlank() || password.isBlank()) {
                _loginScreenViewState.emit(LoginScreenUiState.Failure("Field cannot be empty"))
                return@launch
            }
            _loginScreenViewState.emit(LoginScreenUiState.Loading)
            delay(timeMillis = 500L)
            val user: User? = userDao.getUser(userName, password)
            if (user == null) {
                _loginScreenViewState.emit(LoginScreenUiState.Failure("Invalid username of password"))
                return@launch
            }
            expenseAssistantRepository.setUser(
                UserModel(
                    user.name,
                    user.bankAccount
                )
            )
            _loginScreenViewState.emit(LoginScreenUiState.Success)
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
            return@CoroutineExceptionHandler
        }
        viewModelScope.launch(handler) {
            withContext(dispatcher) {
                if (userName.isBlank() || password.isBlank()) {
                    _loginScreenViewState.emit(LoginScreenUiState.Failure("User name & password cannot be empty"))
                    return@withContext
                }
                if (bankAccounts.isEmpty()) {
                    _loginScreenViewState.emit(LoginScreenUiState.Failure("Please add bank account"))
                    return@withContext
                }
                _loginScreenViewState.emit(LoginScreenUiState.Loading)
                delay(timeMillis = 500L)
                val user = User(
                    name = userName,
                    password = password,
                    bankAccount = bankAccounts
                )
                userDao.setUser(user)
                expenseAssistantRepository.setUser(
                    UserModel(
                        user.name,
                        user.bankAccount
                    )
                )
                _loginScreenViewState.emit(LoginScreenUiState.Success)
            }
        }
    }
}
