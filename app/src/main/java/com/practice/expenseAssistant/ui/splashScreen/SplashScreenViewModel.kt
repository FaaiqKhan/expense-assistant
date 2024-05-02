package com.practice.expenseAssistant.ui.splashScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.expenseAssistant.data.datasource.localStore.StoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val _isSessionExpired = MutableStateFlow<Boolean?>(null)
    val isSessionExpired = _isSessionExpired.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            val sessionExpiry = StoreManager.getLongValue(context = context, key = "last_login")
            if (sessionExpiry == null) {
                _isSessionExpired.value = true
            } else {
                if (sessionExpiry > LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) {
                    _isSessionExpired.value = false
                }
            }
        }
    }
}