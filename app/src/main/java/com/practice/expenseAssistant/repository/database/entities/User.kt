package com.practice.expenseAssistant.repository.database.entities

import androidx.room.*
import com.practice.expenseAssistant.data.BankAccount

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val password: String,
    @ColumnInfo("bank_account")
    val bankAccount: List<BankAccount>
)