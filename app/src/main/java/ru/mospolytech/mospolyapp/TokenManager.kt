package ru.mospolytech.mospolyapp

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "MyPrefs"
    private const val TOKEN_KEY = "token"

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var token: String
        get() = sharedPreferences.getString(TOKEN_KEY, "") ?: ""
        set(value) = sharedPreferences.edit().putString(TOKEN_KEY, value).apply()
}               //do login and password saving too. or not? token is probably nuff