package com.pklproject.checkincheckout.ui.model


import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName("status_absen")
    val statusAbsen: String,
    @SerializedName("tanggal_absen")
    val tanggalAbsen: String,
    @SerializedName("tipe_absen")
    val tipeAbsen: String,
    @SerializedName("waktu_absen")
    val waktuAbsen: String
)