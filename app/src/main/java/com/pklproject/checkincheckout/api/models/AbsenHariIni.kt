package com.pklproject.checkincheckout.api.models


import com.google.gson.annotations.SerializedName

data class AbsenHariIni(
    @SerializedName("status_absen")
    val statusAbsen: String?,
    @SerializedName("tanggal_absen")
    val tanggalAbsen: String?,
    @SerializedName("tipe_absen")
    val tipeAbsen: String?,
    @SerializedName("waktu_absen")
    val waktuAbsen: String?
)