package com.practice.expenseAssistant.utils.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practice.expenseAssistant.data.BankAccount

class BankAccountTypeConverter {

    @TypeConverter
    fun bankAccountToString(bankAccount: BankAccount): String = Gson().toJson(bankAccount)

    @TypeConverter
    fun stringToBankAccount(value: String): BankAccount = Gson().fromJson(
        value,
        object : TypeToken<BankAccount>() {}.type
    )

    @TypeConverter
    fun bankAccountsToString(bankAccounts: List<BankAccount>): String =
        Gson().toJson(bankAccounts)

    @TypeConverter
    fun stringToBankAccounts(value: String): List<BankAccount> = Gson().fromJson(
        value,
        object : TypeToken<List<BankAccount>>() {}.type
    )
}