package com.practice.expenseAssistant.repository.database.entities

import androidx.room.*
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.utils.CategoryType
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "transaction_note")
    val note: String,
    @ColumnInfo(name = "back_account")
    val backAccount: BankAccount,
    @ColumnInfo(name = "category_type")
    val categoryType: CategoryType,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "transaction_date")
    val date: LocalDate,
    @ColumnInfo(name = "transaction_time")
    val time: LocalTime
)
