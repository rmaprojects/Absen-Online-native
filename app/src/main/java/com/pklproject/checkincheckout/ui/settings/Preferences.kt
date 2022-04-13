package com.pklproject.checkincheckout.ui.settings

import android.content.Context
import android.preference.PreferenceManager

class Preferences (context: Context) {

    private companion object {
        const val LOGIN_KEY = "LOGINKEYVALUE"
        const val THEME_KEY = "THEMEKEYVALUE"
        const val NAME_EMPLOYEE_KEY = "NAMEEMPLOYEEKEYVALUE"
    }

    private val loggedIn = PreferenceManager.getDefaultSharedPreferences(context)
    var isLoggedIn = loggedIn.getBoolean(LOGIN_KEY, false)
        set(value) = loggedIn.edit().putBoolean(LOGIN_KEY, value).apply()

    private val changedTheme = PreferenceManager.getDefaultSharedPreferences(context)
    var changeTheme = changedTheme.getInt(THEME_KEY, 0)
        set(value) = changedTheme.edit().putInt(THEME_KEY, value).apply()

    private val nameEmployee = PreferenceManager.getDefaultSharedPreferences(context)
    var employeeName = nameEmployee.getString(NAME_EMPLOYEE_KEY, "404 Not Found")
        set(value) = nameEmployee.edit().putString(NAME_EMPLOYEE_KEY, value).apply()
}