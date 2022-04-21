package com.pklproject.checkincheckout.api.models.preferencesmodel

import com.chibatching.kotpref.KotprefModel

object ThemePreferences : KotprefModel() {
    var theme by intPref(0)
}