package com.practice.expenseAssistant.ui.profileScreen

import androidx.lifecycle.ViewModel
import com.practice.expenseAssistant.data.UserModel
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val expenseAssistantRepository: ExpenseAssistantRepository) :
    ViewModel() {
        fun getUser(): UserModel {
            return expenseAssistantRepository.getUser()
        }
}