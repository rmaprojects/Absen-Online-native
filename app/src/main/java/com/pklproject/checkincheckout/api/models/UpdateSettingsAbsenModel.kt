package com.pklproject.checkincheckout.api.models


import com.google.gson.annotations.SerializedName

data class UpdateSettingsAbsenModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)