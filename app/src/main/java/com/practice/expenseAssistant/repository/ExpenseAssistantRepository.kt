package com.practice.expenseAssistant.repository

import com.practice.expenseAssistant.data.UserModel
import com.practice.expenseAssistant.repository.database.entities.User

interface ExpenseAssistantRepository {
    fun setUser(user: UserModel)
    fun getUser(): UserModel
}