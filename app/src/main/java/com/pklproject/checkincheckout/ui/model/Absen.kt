package com.pklproject.checkincheckout.ui.model


import com.google.gson.annotations.SerializedName

data class Absen(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("tipe_absen")
    val tipeAbsen: String
)