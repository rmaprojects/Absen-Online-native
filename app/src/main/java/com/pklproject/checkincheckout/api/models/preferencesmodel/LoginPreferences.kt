package com.pklproject.checkincheckout.api.models.preferencesmodel

import com.chibatching.kotpref.KotprefModel

object LoginPreferences : KotprefModel() {
    var businessUnit by stringPref()
    var code by stringPref()
    var departement by stringPref()
    var idKaryawan by stringPref()
    var jabatan by stringPref()
    var message by stringPref()
    var namaKaryawan by stringPref()
    var status by stringPref()
    var statusAdmin by stringPref()
    var statusKaryawan by stringPref()
    var username by stringPref()
    var password by stringPref()
}