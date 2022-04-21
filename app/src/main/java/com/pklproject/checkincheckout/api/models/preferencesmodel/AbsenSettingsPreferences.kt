package com.pklproject.checkincheckout.api.models.preferencesmodel

import com.chibatching.kotpref.KotprefModel
import com.google.gson.annotations.SerializedName

object AbsenSettingsPreferences : KotprefModel() {
    var absenPagiAkhir by stringPref()
    var absenPagiAwal by stringPref()
    var absenPulangAkhir by stringPref()
    var absenPulangAwal by stringPref()
    var absenSiangAkhir by stringPref()
    var absenSiangAwal by stringPref()
    var absenSiangDiperlukan by stringPref()
}