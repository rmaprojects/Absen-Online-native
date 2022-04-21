package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class AbsenHariIni(
    @SerializedName("absen_yang_dibutuhkan")
    val absenYangDibutuhkan: String?,
    @SerializedName("absen_siang_diperlukan")
    val absenSiangDiperlukan:String?,
    @SerializedName("cuti")
    val cuti: String?,
    @SerializedName("izin")
    val izin: String?,
    @SerializedName("jam_masuk_pagi")
    val jamMasukPagi: String?,
    @SerializedName("jam_masuk_pulang")
    val jamMasukPulang: String?,
    @SerializedName("jam_masuk_siang")
    val jamMasukSiang: String?,
    @SerializedName("tanggal")
    val tanggal: String?
)