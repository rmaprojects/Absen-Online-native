package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class Hasil(
    @SerializedName("persentase_izin_atau_cuti")
    val persentaseIzinAtauCuti: Float?,
    @SerializedName("persentase_on_time")
    val persentaseOnTime: Float?,
    @SerializedName("persentase_telat_atau_tidak_absen")
    val persentaseTelatAtauTidakAbsen: Float?,
    @SerializedName("persentase_tidak_hadir")
    val persentaseTidakHadir: Float?
)