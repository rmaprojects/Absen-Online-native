package com.pklproject.checkincheckout.ui.model


import com.google.gson.annotations.SerializedName

data class HistoryModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("history")
    val history: List<History>,
    @SerializedName("message")
    val message: String,
    @SerializedName("nama_karyawan")
    val namaKaryawan: String,
    @SerializedName("status")
    val status: Boolean
)