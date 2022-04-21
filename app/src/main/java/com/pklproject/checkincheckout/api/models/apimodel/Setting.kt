package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class Setting(
    @SerializedName("absen_pagi_akhir")
    val absenPagiAkhir: String,
    @SerializedName("absen_pagi_awal")
    val absenPagiAwal: String,
    @SerializedName("absen_pulang_akhir")
    val absenPulangAkhir: String,
    @SerializedName("absen_pulang_awal")
    val absenPulangAwal: String,
    @SerializedName("absen_siang_akhir")
    val absenSiangAkhir: String,
    @SerializedName("absen_siang_awal")
    val absenSiangAwal: String,
    @SerializedName("absen_siang_diperlukan")
    val absenSiangDiperlukan: String
)