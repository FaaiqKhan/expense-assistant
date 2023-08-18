package com.practice.expenseAssistant.data

import com.practice.expenseAssistant.utils.CurrencyType

data class UserModel(
    val name: String,
    val bankAccounts: List<BankAccount>,
    val currencyType: CurrencyType,
    val selectedBankAccount: BankAccount,
    val transactions: List<TransactionModel>
)