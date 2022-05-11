package com.pklproject.checkincheckout.api.models.apimodel


import com.google.gson.annotations.SerializedName

data class ServerDateModel(
    @SerializedName("clock")
    val clock: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("date_now")
    val dateNow: String
)