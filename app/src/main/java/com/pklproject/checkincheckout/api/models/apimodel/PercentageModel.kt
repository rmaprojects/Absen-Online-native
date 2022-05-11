package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class PercentageModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("hasil")
    val hasil: Hasil,
    @SerializedName("message")
    val message: String,
    @SerializedName("persentase")
    val persentase: Persentase,
    @SerializedName("range_tanggal")
    val rangeTanggal: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("total_hari")
    val totalHari: Int,
    @SerializedName("username")
    val username: String
)