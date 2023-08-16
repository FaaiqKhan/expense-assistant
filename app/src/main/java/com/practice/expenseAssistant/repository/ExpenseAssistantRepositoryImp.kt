package com.practice.expenseAssistant.repository

import com.practice.expenseAssistant.data.UserModel
import javax.inject.Inject

class ExpenseAssistantRepositoryImp @Inject constructor() :
    ExpenseAssistantRepository {

    private lateinit var user: UserModel
    override fun setUser(user: UserModel) {
        this.user = user
    }

    override fun getUser(): UserModel = user

}