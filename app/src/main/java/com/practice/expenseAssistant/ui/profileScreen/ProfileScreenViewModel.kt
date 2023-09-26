package com.practice.expenseAssistant.ui.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.UserModel
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.repository.database.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val userDao: UserDao,
    private val expenseAssistantRepository: ExpenseAssistantRepository
) : ViewModel() {

    private val _viewActions = MutableStateFlow<ViewActions?>(null)
    val viewActions = _viewActions.asStateFlow()

    fun getUser(): UserModel = expenseAssistantRepository.getUser()

    fun updatePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                userDao.updatePassword(
                    expenseAssistantRepository.getUser().name,
                    oldPassword,
                    newPassword
                )
                _viewActions.emit(ViewActions.PasswordUpdated)
            } catch (exception: Exception) {
                _viewActions.emit(ViewActions.IncorrectPassword)
            }
        }
    }
}

sealed interface ViewActions {
    object PasswordUpdated : ViewActions
    object IncorrectPassword : ViewActions
}