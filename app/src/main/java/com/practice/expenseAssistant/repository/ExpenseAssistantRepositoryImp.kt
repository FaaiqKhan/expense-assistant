package com.practice.expenseAssistant.repository

import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.repository.database.dao.TransactionDao
import com.practice.expenseAssistant.repository.database.entities.Transaction
import javax.inject.Inject

class ExpenseAssistantRepositoryImp @Inject constructor(
    private val transactionDao: TransactionDao,
) : ExpenseAssistantRepository {

    private lateinit var user: UserModel
    override fun setUser(user: UserModel) {
        this.user = user
    }

    override fun getUser(): UserModel {
        return user
    }

    override suspend fun addTransaction(transaction: TransactionModel, bankAccount: BankAccount) {
        transactionDao.addTransaction(
            Transaction(
                amount = transaction.amount,
                note = transaction.note,
                backAccount = bankAccount,
                categoryType = transaction.categoryType,
                category = transaction.category.toString(),
                date = transaction.date,
                time = transaction.time
            )
        )
    }

    override fun getTransaction(transactionId: Int): TransactionModel? =
        user.transactions.find { it.transactionId == transactionId }

    override fun getTransactions(): List<TransactionModel> = user.transactions

}