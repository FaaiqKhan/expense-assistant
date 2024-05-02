package com.practice.expenseAssistant.data.datasource.localStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*

private val Context.userSession: DataStore<Preferences> by preferencesDataStore(name = "user")

object StoreManager {

    suspend fun saveLongValue(context: Context, key: String, value: Long) {
        val wrappedKey = longPreferencesKey(key)
        context.userSession.edit { it[wrappedKey] = value }
    }

    suspend fun getLongValue(context: Context, key: String): Long? {
        val wrappedKey = longPreferencesKey(key)
        val valueFlow: Flow<Long?> = context.userSession.data.map { it[wrappedKey] }
        return valueFlow.first()
    }
}