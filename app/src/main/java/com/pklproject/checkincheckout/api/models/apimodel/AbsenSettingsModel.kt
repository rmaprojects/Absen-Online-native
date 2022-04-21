package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class AbsenSettingsModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("setting")
    val setting: Setting,
    @SerializedName("status")
    val status: Boolean
)