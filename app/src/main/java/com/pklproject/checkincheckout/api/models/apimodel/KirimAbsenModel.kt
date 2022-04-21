package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class KirimAbsenModel(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("tipe_absen")
    val tipeAbsen: String?,
    @SerializedName("id_absensi")
    val idAbsensi:String?
)