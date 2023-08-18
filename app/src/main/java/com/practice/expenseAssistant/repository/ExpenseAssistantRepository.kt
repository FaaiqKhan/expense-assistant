package com.practice.expenseAssistant.repository

import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.database.entities.User

interface ExpenseAssistantRepository {

    fun setUser(user: UserModel)
    fun getUser(): UserModel
    suspend fun addTransaction(transaction: TransactionModel, bankAccount: BankAccount)
    fun getTransaction(transactionId: Int): TransactionModel?
    fun getTransactions(): List<TransactionModel>
}