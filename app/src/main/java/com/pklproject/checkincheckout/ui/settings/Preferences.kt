package com.pklproject.checkincheckout.ui.settings

import android.content.Context
import android.preference.PreferenceManager

class Preferences (context: Context) {

    private companion object {
        const val LOGINKEY = "LOGINKEYVALUE"
        const val THEMEKEY = "THEMEKEYVALUE"
        const val NAMEEMPLOYEEKEY = "NAMEEMPLOYEEKEYVALUE"
        const val USERNAMEKEY = "USERNAMEKEYVALUE"
        const val PASSWORDKEY = "PASSWORDKEYVALUE"
    }

    private val loggedIn = PreferenceManager.getDefaultSharedPreferences(context)
    var isLoggedIn = loggedIn.getBoolean(LOGINKEY, false)
        set(value) = loggedIn.edit().putBoolean(LOGINKEY, value).apply()

    private val changedTheme = PreferenceManager.getDefaultSharedPreferences(context)
    var changeTheme = changedTheme.getInt(THEMEKEY, 0)
        set(value) = changedTheme.edit().putInt(THEMEKEY, value).apply()

    private val nameEmpoyee = PreferenceManager.getDefaultSharedPreferences(context)
    var employeeName = nameEmpoyee.getString(NAMEEMPLOYEEKEY, "404 Not Found")
        set(value) = nameEmpoyee.edit().putString(NAMEEMPLOYEEKEY, value).apply()

    private val usernameStorage = PreferenceManager.getDefaultSharedPreferences(context)
    var username = usernameStorage.getString("username", "404 Not Found")
        set(value) = usernameStorage.edit().putString(USERNAMEKEY, value).apply()

    private val passwordStorage = PreferenceManager.getDefaultSharedPreferences(context)
    var password = passwordStorage.getString("password", "404 Not Found")
        set(value) = passwordStorage.edit().putString(PASSWORDKEY, value).apply()
}