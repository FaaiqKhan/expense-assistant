package com.practice.expenseAssistant.di

import com.practice.expenseAssistant.repository.ExpenseAssistantRepository
import com.practice.expenseAssistant.repository.ExpenseAssistantRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
fun interface ExpenseAssistantModule {

    @Binds
    @Singleton
    fun bindExpenseAssistantRepo(expenseAssistantRepositoryImp: ExpenseAssistantRepositoryImp): ExpenseAssistantRepository
}