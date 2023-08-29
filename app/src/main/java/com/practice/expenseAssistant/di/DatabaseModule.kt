package com.practice.expenseAssistant.di

import android.content.Context
import androidx.room.Room
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.repository.database.AppDatabase
import com.practice.expenseAssistant.repository.database.dao.*
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.getUserDao()

    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao =
        appDatabase.getTransactionDao()

    @Provides
    fun provideCashFLowDao(appDatabase: AppDatabase): CashFlowDao = appDatabase.getCashFlowDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = Room
        .databaseBuilder(
            context,
            AppDatabase::class.java,
            context.getString(R.string.expense_assistant)
        )
        .fallbackToDestructiveMigration()
        .build()
}