package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class Hasil(
    @SerializedName("persentase_hadir")
    val persentaseHadir: Float?,
    @SerializedName("persentase_izin_atau_cuti")
    val persentaseIzinAtauCuti: Float?,
    @SerializedName("persentase_telat")
    val persentaseTelat: Float?,
    @SerializedName("persentase_tidak_full_absen")
    val persentaseTidakFullAbsen: Float?,
    @SerializedName("persentase_tidak_hadir")
    val persentaseTidakHadir: Float?,
    @SerializedName("total_hari")
    val totalHari: Float?
)