package com.practice.expenseAssistant.repository.database.dao

import androidx.room.*
import com.practice.expenseAssistant.repository.database.entities.Transaction
import com.practice.expenseAssistant.utils.CategoryType
import java.time.LocalDate
import java.time.LocalTime

@Dao
interface TransactionDao {

    @Insert
    suspend fun addTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Query("DELETE FROM `transaction` WHERE transaction_date = :date AND transaction_time = :time AND user_id = :userId")
    suspend fun removeTransaction(date: LocalDate, time: LocalTime, userId: Int)

    @Query("SELECT * from `transaction` WHERE user_id = :userId")
    suspend fun getAllTransactions(userId: Int): List<Transaction>

    @Query("SELECT * from `transaction` WHERE category_type = :categoryType AND user_id = :userId")
    suspend fun getTransactionsByCategory(categoryType: CategoryType, userId: Int): List<Transaction>
}