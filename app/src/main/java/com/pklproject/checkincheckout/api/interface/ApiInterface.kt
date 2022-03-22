package com.pklproject.checkincheckout.api.`interface`

import com.pklproject.checkincheckout.api.models.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {

    //CONTOH:
    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field ("username") username: String,
        @Field ("password") password: String
    ): LoginModel

    @POST("absen.php")
    suspend fun kirimAbsen(
        @Part username: String,
        @Part password: String,
        @Part tipe_absen: String,
        @Part longitude: Double,
        @Part latitude: Double,
        @Part photo_name: String,
        @Part keterangan: String
    ): Response<AbsenModel>

    @POST("history_absen.php")
    suspend fun history(
        @Part username: String,
        @Part password: String,
        @Part tahun: Int,
        @Part Bulan: String
    ): Response<HistoryModel>

    @GET("absen_settings.php")
    suspend fun absenSettings(
    ): Response<AbsenSettingsModel>

    companion object {
        private const val BASE_URL: String = "http://10.10.22.147/api_absen/"

        fun createApi(): ApiInterface {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}