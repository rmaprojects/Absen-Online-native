package com.pklproject.checkincheckout.ui.settings

import android.content.Context
import android.preference.PreferenceManager

class Preferences (context: Context) {

    private companion object {
        const val LOGINKEY = "LOGINKEYVALUE"
    }

    private val loggedIn = PreferenceManager.getDefaultSharedPreferences(context)
    var isLoggedIn = loggedIn.getBoolean(LOGINKEY, false)
        set(value) = loggedIn.edit().putBoolean(LOGINKEY, value).apply()
}