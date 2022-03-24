package com.pklproject.checkincheckout.api.models


import com.google.gson.annotations.SerializedName

data class TodayAttendanceModel(
    @SerializedName("absen_hari_ini")
    val absenHariIni: List<AbsenHariIni>?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?
)