package com.practice.expenseAssistant.repository.database.entities

import androidx.room.*
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.data.MonthCashFlow
import com.practice.expenseAssistant.utils.CurrencyType

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val name: String,
    val password: String,
    @ColumnInfo("bank_account")
    val bankAccount: List<BankAccount>,
    @ColumnInfo("currency_type")
    val currencyType: CurrencyType,
    @ColumnInfo("default_bank_account")
    val selectedBankAccount: BankAccount,
)