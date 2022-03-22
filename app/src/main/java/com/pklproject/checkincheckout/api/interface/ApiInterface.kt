package com.pklproject.checkincheckout.api.`interface`

import com.pklproject.checkincheckout.api.models.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    //CONTOH:
    @POST("login.php")
    suspend fun login(
        @Body username: String,
        @Body password: String
    ): Response<LoginModel>

    @POST("absen.php")
    suspend fun kirimAbsen(
        @Body username: String,
        @Body password: String,
        @Body tipe_absen: String,
        @Body longitude: Double,
        @Body latitude: Double,
        @Body photo_name: String,
        @Body keterangan: String
    ): Response<AbsenModel>

    @POST("history_absen.php")
    suspend fun history(
        @Body username: String,
        @Body password: String,
        @Body tahun: Int,
        @Body Bulan: String
    ): Response<HistoryModel>

    @GET("absen_settings.php")
    suspend fun absenSettings(
    ): Response<AbsenSettingsModel>

    companion object {
        private const val BASE_URL: String = "10.10.22.147/api_absen/"

        fun createApi(): ApiInterface {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}