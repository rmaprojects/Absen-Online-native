package com.pklproject.checkincheckout.api.`interface`

import com.pklproject.checkincheckout.api.models.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    //CONTOH:
    @POST("Endpoint")
    suspend fun login(
        @Body username:String,
        @Body password:String
    ) : Response<LoginModel>

    @POST("username")
    suspend fun postusername(
        @Body data: Login
    ):Response<Unit>

    @POST("password")
    suspend fun postpassword(
        @Body data: Login
    ):Response<Unit>

    @POST("username")
    suspend fun postusername(
        @Body data: AbsenModel
    ):Response<Unit>

    @POST("password")
    suspend fun postpassword(
        @Body data: AbsenModel
    ):Response<Unit>

    @POST("tipe_absen")
    suspend fun posttipeabsen(
        @Body data: AbsenModel
    ):Response<Unit>

    @POST("longitude")
    suspend fun postlongitude(
        @Body data: AbsenModel
    ):Response<Unit>

    @POST("latitude")
    suspend fun postlatitude(
        @Body data: AbsenModel
    ):Response<Unit>

    @POST("photo_name")
    suspend fun postphoto_name(
        @Body data: AbsenModel
    ):Response<Unit>

    @POST("keterangan")
    suspend fun postketerangan(
        @Body data: AbsenModel
    ):Response<Unit>

    @POST("username")
    suspend fun postusername(
        @Body data: HistoryModel
    ):Response<Unit>

    @POST("password")
    suspend fun postpassword(
        @Body data: HistoryModel
    ):Response<Unit>

    @POST("tahun")
    suspend fun posttahun(
        @Body data: HistoryModel
    ):Response<Unit>

    @POST("bulan")
    suspend fun postbulan(
        @Body data: HistoryModel
    ):Response<Unit>
    companion object{
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