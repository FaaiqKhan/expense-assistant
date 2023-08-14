package com.practice.expenseAssistant.repository.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.practice.expenseAssistant.repository.database.entities.Transaction
import com.practice.expenseAssistant.utils.CategoryType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * from `transaction` WHERE category_type = :categoryType")
    fun getTransactionsByCategory(categoryType: CategoryType): Flow<List<Transaction>>

    @Query("SELECT * from `transaction`")
    fun getAllTransactions(): Flow<List<Transaction>>
}