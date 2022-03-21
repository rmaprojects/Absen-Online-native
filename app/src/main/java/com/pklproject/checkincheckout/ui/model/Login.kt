package com.pklproject.checkincheckout.ui.model


import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("code")
    val code: Int,
    @SerializedName("login")
    val login: List<LoginX>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)