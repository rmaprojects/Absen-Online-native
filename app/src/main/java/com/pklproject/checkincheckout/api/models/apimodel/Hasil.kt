package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class Hasil(
    @SerializedName("persentase_hadir")
    val persentaseHadir: Int?,
    @SerializedName("persentase_izin_atau_cuti")
    val persentaseIzinAtauCuti: Int?,
    @SerializedName("persentase_telat")
    val persentaseTelat: Int?,
    @SerializedName("persentase_tidak_full_absen")
    val persentaseTidakFullAbsen: Int?,
    @SerializedName("persentase_tidak_hadir")
    val persentaseTidakHadir: Int?,
    @SerializedName("total_hari")
    val totalHari: Int?
)