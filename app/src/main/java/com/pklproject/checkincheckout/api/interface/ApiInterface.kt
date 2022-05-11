package com.pklproject.checkincheckout.api.`interface`

import com.google.gson.GsonBuilder
import com.pklproject.checkincheckout.api.models.apimodel.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("absen.php")
    suspend fun kirimAbsen(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part("tipe_absen") tipeAbsen: RequestBody,
        @Part("longitude") longitude: RequestBody?,
        @Part("latitude") latitude: RequestBody?,
        @Part photo_absen: MultipartBody.Part?,
        @Part("keterangan") keterangan: RequestBody?,
        @Part("jam_masuk") waktuMasuk: RequestBody,
        @Part("tanggal_sekarang") tanggalSekarang: RequestBody?
    ): Response<KirimAbsenModel>

    @FormUrlEncoded
    @POST("history_absen.php")
    suspend fun history(
        @Field ("username") username: String,
        @Field ("password") password: String,
        @Field ("tahun") tahun: String,
        @Field ("bulan") bulan: String
    ): Response<HistoryAbsenModel>

    @GET("absen_settings.php")
    suspend fun getAbsenSettings(
    ): AbsenSettingsModel

    @FormUrlEncoded
    @POST("cek_absen_hari_ini.php")
    suspend fun cekAbsenHariIni(
        @Field ("username") username: String?,
        @Field ("password") password: String?,
        @Field ("tanggal_sekarang") today:String
    ): TodayAttendanceModel

    @FormUrlEncoded
    @POST("update_absen_settings.php")
    suspend fun updateSettingsAbsen(
        @Field("tipe_absen") tipeAbsen: String,
        @Field ("absen_awal") absenAwal :String?,
        @Field ("absen_akhir") AbsenAkhir :String?
    ): UpdateSettingsAbsenModel

    @GET("date_now.php")
    suspend fun getDateNow(): Response<ServerDateModel>

    @FormUrlEncoded
    @POST("persentase.php")
    suspend fun getPersentaseKaryawan(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("tanggal_awal") tanggalAwal: String,
        @Field("tanggal_akhir") tanggalAkhir: String,
        @Field("filter") filterPersentase: String
    ) : Response<PercentageModel>

    companion object {
        private const val BASE_URL: String = "https://absen-dev.omgindo.com/"

        fun createApi(): ApiInterface {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .build()
                .create(ApiInterface::class.java)
        }
    }
}