package com.pklproject.checkincheckout.api.`interface`

import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
    ): Response<LoginModel>

    @FormUrlEncoded
    @POST("absen.php")
    suspend fun kirimAbsen(
        @Field ("username") username: String,
        @Field ("password") password: String,
        @Field ("tipe_absen") tipe_absen: String,
        @Field ("longitude") longitude: Double,
        @Field ("latitude") latitude: Double,
        @Field ("photo_name") photo_name: String?,
        @Field ("keterangan") keterangan: String
    ): KirimAbsenModel

    @FormUrlEncoded
    @POST("history_absen.php")
    suspend fun history(
        @Field ("username") username: String,
        @Field ("password") password: String,
        @Field ("tahun") tahun: Int,
        @Field ("bulan") bulan: String
    ): Response<HistoryModel>

    @GET("absen_settings.php")
    suspend fun getAbsenSettings(
    ): AbsenSettingsModel

    @FormUrlEncoded
    @POST("cek_absen_hari_ini.php")
    suspend fun cekAbsenHariIni(
        @Field ("username") username: String,
        @Field ("password") password: String,
        @Field ("tanggal_sekarang") today:String
    ):TodayAttendanceModel

    companion object {
        private const val BASE_URL: String = "http://10.10.22.147/api_absen/"

        val gson:Gson = GsonBuilder().setLenient().create()

        fun createApi(): ApiInterface {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiInterface::class.java)
        }
    }
}