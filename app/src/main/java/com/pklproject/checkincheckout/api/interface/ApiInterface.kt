package com.pklproject.checkincheckout.api.`interface`

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Query

interface ApiInterface {

    companion object{
        private const val BASE_URL: String = "10.10.22.147/api_absen"

        fun create(): ApiInterface{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
            return retrofit
        }
    }
}