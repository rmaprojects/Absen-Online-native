package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class Persentase(
    @SerializedName("persentase_kehadiran")
    val persentaseKehadiran: Float?,
    @SerializedName("persentase_ketidakhadiran")
    val persentaseKetidakhadiran: Float?
)