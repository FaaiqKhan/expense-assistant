package com.practice.expenseAssistant.ui.loginScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.data.UserModel
import com.practice.expenseAssistant.data.datasource.database.dao.UserDao
import com.practice.expenseAssistant.data.datasource.database.entities.CashFlow
import com.practice.expenseAssistant.data.datasource.database.entities.User
import com.practice.expenseAssistant.data.datasource.localStore.StoreManager
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.utils.CurrencyType
import com.practice.expenseAssistant.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val userDao: UserDao,
    private val repository: ExpenseAssistantRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _loginScreenViewState = MutableStateFlow<LoginScreenUiState>(
        LoginScreenUiState.Ideal
    )
    val loginScreenUiState: StateFlow<LoginScreenUiState> = _loginScreenViewState.asStateFlow()

    fun signIn(userName: String, password: String) {
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
            val transactions = repository.fetchAllTransactionsOfUser(user.id)
            repository.setUser(
                UserModel(
                    id = user.id,
                    name = user.name,
                    transactions = transactions,
                    bankAccounts = user.bankAccount,
                    currencyType = user.currencyType,
                    selectedBankAccount = user.selectedBankAccount,
                )
            )
            val monthCashFlow = repository.fetchCashFlowOfMonthFromDB(
                month = repository.getSelectedDate().monthValue,
                year = repository.getSelectedDate().year,
            )
            if (monthCashFlow == null) {
                val selectedDate = repository.getSelectedDate()
                val cashFlow = CashFlow(
                    userId = user.id,
                    year = selectedDate.year,
                    month = selectedDate.monthValue,
                    openingAmount = user.selectedBankAccount.balance,
                    closingAmount = user.selectedBankAccount.balance,
                )
                repository.insertCashFlowIntoDb(cashFlow)
            } else {
                repository.setMonthCashFLow(monthCashFlow)
            }
            val calendar = Utils.createCalenderDays(
                transactions = transactions,
                year = repository.getSelectedDate().year,
                month = repository.getSelectedDate().monthValue,
                date = repository.getSelectedDate().dayOfMonth,
            )
            repository.updateCalendar(calendar)
            StoreManager.saveLongValue(
                context = context,
                key = "last_login",
                value = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            )
            _loginScreenViewState.emit(LoginScreenUiState.Success)
        }
    }

    fun signUp(
        userName: String,
        password: String,
        bankAccounts: List<BankAccount>,
        selectedBankAccount: BankAccount,
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
                    bankAccount = bankAccounts,
                    currencyType = CurrencyType.Dollar,
                    selectedBankAccount = selectedBankAccount
                )
                userDao.setUser(user)
                repository.setUser(
                    UserModel(
                        name = user.name,
                        bankAccounts = user.bankAccount,
                        currencyType = user.currencyType,
                        selectedBankAccount = user.selectedBankAccount,
                        id = user.id
                    )
                )
                val selectedDate = repository.getSelectedDate()
                val cashFlow = CashFlow(
                    userId = user.id,
                    year = selectedDate.year,
                    month = selectedDate.monthValue,
                    openingAmount = selectedBankAccount.balance,
                    closingAmount = selectedBankAccount.balance,
                )
                repository.insertCashFlowIntoDb(cashFlow)
                val calendar = Utils.createCalenderDays(
                    year = repository.getSelectedDate().year,
                    month = repository.getSelectedDate().monthValue,
                    date = repository.getSelectedDate().dayOfMonth,
                )
                repository.updateCalendar(calendar)
                _loginScreenViewState.emit(LoginScreenUiState.Success)
            }
        }
    }

    fun updateState(state: LoginScreenUiState) {
        viewModelScope.launch {
            _loginScreenViewState.emit(state)
        }
    }
}
