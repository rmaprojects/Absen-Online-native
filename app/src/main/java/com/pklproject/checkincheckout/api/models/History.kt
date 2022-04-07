package com.pklproject.checkincheckout.api.models


import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName("absen_siang_diperlukan")
    val absenSiangDiperlukan: String?,
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
    @SerializedName("status_keterlambatan_pagi")
    val statusKeterlambatanPagi: String?,
    @SerializedName("status_keterlambatan_siang")
    val statusKeterlambatanSiang: String?,
    @SerializedName("status_keterlambatan_pulang")
    val statusKeterlambatanPulang: String?,
    @SerializedName("tanggal")
    val tanggal: String?
)