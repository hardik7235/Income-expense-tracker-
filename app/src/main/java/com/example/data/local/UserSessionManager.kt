package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session_prefs", Context.MODE_PRIVATE)

    private val _currentUserId = MutableStateFlow(prefs.getLong(KEY_LOGGED_IN_USER_ID, 1L))
    val currentUserId: StateFlow<Long> = _currentUserId.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(prefs.getBoolean(KEY_IS_LOGGED_IN, false))
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun setLoggedInUser(userId: Long) {
        prefs.edit()
            .putLong(KEY_LOGGED_IN_USER_ID, userId)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
        _currentUserId.value = userId
        _isLoggedIn.value = true
    }

    fun logout() {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
        _isLoggedIn.value = false
    }

    companion object {
        private const val KEY_LOGGED_IN_USER_ID = "logged_in_user_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
}
