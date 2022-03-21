package com.pklproject.checkincheckout.ui.model


import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("login")
    val login: List<Login>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)