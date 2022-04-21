package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("business_unit")
    val businessUnit: String?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("departement")
    val departement: String?,
    @SerializedName("id_karyawan")
    val idKaryawan: String?,
    @SerializedName("jabatan")
    val jabatan: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("nama_karyawan")
    val namaKaryawan: String?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("status_admin")
    val statusAdmin: String?,
    @SerializedName("status_karyawan")
    val statusKaryawan: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("password")
    val password: String?
)