package com.practice.expenseAssistant.data.datasource.database.entities

import androidx.room.*
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.utils.CategoryType
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("amount")
    val amount: Double,
    @ColumnInfo("transaction_note")
    val note: String,
    @ColumnInfo("back_account")
    val backAccount: BankAccount,
    @ColumnInfo("category_type")
    val categoryType: CategoryType,
    @ColumnInfo("category")
    val category: String,
    @ColumnInfo("transaction_date")
    val date: LocalDate,
    @ColumnInfo("transaction_time")
    val time: LocalTime,
    @ColumnInfo("user_id")
    val userId: Int,
    @ColumnInfo("transaction_month")
    val month: Int,
    @ColumnInfo("transaction_year")
    val year: Int,
)
