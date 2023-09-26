package com.practice.expenseAssistant.data

import com.practice.expenseAssistant.utils.CurrencyType
import java.time.LocalDate

data class UserModel(
    val id: Int,
    val name: String,
    val bankAccounts: List<BankAccount>,
    val currencyType: CurrencyType,
    val selectedBankAccount: BankAccount,
    val transactions: Map<LocalDate, List<TransactionModel>> = mapOf()
)