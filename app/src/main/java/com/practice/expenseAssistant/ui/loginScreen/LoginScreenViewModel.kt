package com.practice.expenseAssistant.ui.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.repository.database.dao.TransactionDao
import com.practice.expenseAssistant.repository.database.dao.UserDao
import com.practice.expenseAssistant.repository.database.entities.User
import com.practice.expenseAssistant.utils.CurrencyType
import com.practice.expenseAssistant.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val userDao: UserDao,
    private val transactionDao: TransactionDao,
    private val expenseAssistantRepository: ExpenseAssistantRepository,
) : ViewModel() {

    private val _loginScreenViewState =
        MutableStateFlow<LoginScreenUiState>(LoginScreenUiState.Ideal)

    val loginScreenUiState: StateFlow<LoginScreenUiState> = _loginScreenViewState

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
            val transactions: MutableMap<LocalDate, MutableList<TransactionModel>> = mutableMapOf()
            transactionDao.getAllTransactions().forEach {
                val transaction = TransactionModel(
                    transactionId = it.id,
                    categoryType = it.categoryType,
                    category = it.category,
                    note = it.note,
                    amount = it.amount,
                    date = it.date,
                    time = it.time
                )
                if (transactions[it.date] != null) {
                    transactions.getValue(it.date).add(transaction)
                } else {
                    transactions[it.date] = mutableListOf(transaction)
                }
            }
            expenseAssistantRepository.setUser(
                UserModel(
                    name = user.name,
                    bankAccounts = user.bankAccount,
                    currencyType = user.currencyType,
                    selectedBankAccount = user.selectedBankAccount,
                    transactions = transactions
                )
            )
            initCalendar()
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
                expenseAssistantRepository.setUser(
                    UserModel(
                        name = user.name,
                        bankAccounts = user.bankAccount,
                        currencyType = user.currencyType,
                        selectedBankAccount = user.selectedBankAccount,
                    )
                )
                initCalendar()
                _loginScreenViewState.emit(LoginScreenUiState.Success)
            }
        }
    }

    private suspend fun initCalendar() {
        val calendar = Utils.createCalenderDays(
            todayDate = expenseAssistantRepository.getTodayDate(),
            month = expenseAssistantRepository.getCurrentMonth(),
            transactions = expenseAssistantRepository.getUser().transactions,
        )
        expenseAssistantRepository.updateCalendar(calendar)
        expenseAssistantRepository.setCalenderData(
            CalendarDataModel(
                localDate = expenseAssistantRepository.getCurrentMonth(),
                localCalendar = calendar
            ),
        )
    }
}
