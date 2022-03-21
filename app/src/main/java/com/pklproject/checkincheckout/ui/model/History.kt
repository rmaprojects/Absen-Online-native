package com.pklproject.checkincheckout.ui.model


import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName("code")
    val code: Int,
    @SerializedName("history")
    val history: List<HistoryX>,
    @SerializedName("message")
    val message: String,
    @SerializedName("nama_karyawan")
    val namaKaryawan: String,
    @SerializedName("status")
    val status: Boolean
)