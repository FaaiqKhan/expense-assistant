package com.practice.expenseAssistant.repository.database.dao

import androidx.room.*
import com.practice.expenseAssistant.repository.database.entities.Transaction
import com.practice.expenseAssistant.utils.CategoryType

@Dao
interface TransactionDao {

    @Insert
    suspend fun addTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun removeTransaction(transaction: Transaction)

    @Query("SELECT * from `transaction`")
    suspend fun getAllTransactions(): List<Transaction>

    @Query("SELECT * from `transaction` WHERE category_type = :categoryType")
    suspend fun getTransactionsByCategory(categoryType: CategoryType): List<Transaction>
}