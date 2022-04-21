package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class HistoryAbsenModel(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("history")
    val history: List<History>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("nama_karyawan")
    val namaKaryawan: String?,
    @SerializedName("status")
    val status: Boolean?
)