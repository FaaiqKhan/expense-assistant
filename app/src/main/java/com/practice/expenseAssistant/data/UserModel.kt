package com.practice.expenseAssistant.data

import java.net.URL

data class UserModel(
    val name: String,
    val accounts: List<BankAccountModel>,
    val image: URL? = null,
)
