package com.practice.expenseAssistant.ui.categoryScreen

import androidx.lifecycle.ViewModel
import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.utils.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryScreenViewModel @Inject constructor(
    private val expenseAssistantRepository: ExpenseAssistantRepository
) : ViewModel() {

    fun updateCategory(type: CategoryType, category: String) {
        expenseAssistantRepository.updateCategoryAndType(category, type)
    }
}