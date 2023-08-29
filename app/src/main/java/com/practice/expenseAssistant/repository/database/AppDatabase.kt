package com.practice.expenseAssistant.repository.database

import androidx.room.*
import com.practice.expenseAssistant.repository.database.dao.*
import com.practice.expenseAssistant.repository.database.entities.*
import com.practice.expenseAssistant.repository.database.entities.Transaction
import com.practice.expenseAssistant.utils.typeConverters.*

@Database(
    version = 1,
    exportSchema = false,
    entities = [User::class, Transaction::class, CashFlow::class],
)
@TypeConverters(
    value = [
        LocalDateTypeConverter::class,
        LocalTimeTypeConverter::class,
        IntegerListTypeConverter::class,
        BankAccountTypeConverter::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getTransactionDao(): TransactionDao
    abstract fun getCashFlowDao(): CashFlowDao

}