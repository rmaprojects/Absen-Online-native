package com.pklproject.checkincheckout.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otaliastudios.cameraview.PictureResult
import com.pklproject.checkincheckout.api.models.AbsenHariIni
import com.pklproject.checkincheckout.api.models.HistoryAbsenModel
import java.text.SimpleDateFormat
import java.util.*

class ServiceViewModel : ViewModel() {

    private val longitude : MutableLiveData<Double> = MutableLiveData<Double>()
    private val latitude : MutableLiveData<Double> = MutableLiveData<Double>()

    private val resultPicture : MutableLiveData<PictureResult> = MutableLiveData<PictureResult>()
    private val todayAttendance : MutableLiveData<List<AbsenHariIni>> = MutableLiveData<List<AbsenHariIni>>()

    private val month :MutableLiveData<String> = MutableLiveData()
    private val year :MutableLiveData<String> = MutableLiveData()

    private val bitmapImage: MutableLiveData<Bitmap> = MutableLiveData()

    private val historyData : MutableLiveData<HistoryAbsenModel> = MutableLiveData()

    fun getHistoryData() : HistoryAbsenModel? {
        return historyData.value
    }

    fun setHistoryData(historyData: HistoryAbsenModel){
        this.historyData.value = historyData
    }


    fun getTodayAttendance() : List<AbsenHariIni>? {
        return todayAttendance.value
    }

    fun setTodayAttendance(todayAttendance : List<AbsenHariIni>?) {
        this.todayAttendance.value = todayAttendance
    }

    fun getLongitude() : Double? {
        return longitude.value
    }

    fun getLatitude() : Double? {
        return latitude.value
    }

    fun setLatitude(latitude : Double) {
        this.latitude.value = latitude
    }

    fun setLongitude(longitude : Double) {
        this.longitude.value = longitude
    }

    fun getResultPicture() : PictureResult? {
        return resultPicture.value
    }

    fun setResultPicture(resultPicture: PictureResult?) {
        this.resultPicture.value = resultPicture
    }

    fun getMonth() : String? {
        return month.value?: SimpleDateFormat("MM", Locale.getDefault()).format(Date())
    }

    fun setMonth(month : String?) {
        this.month.value = month
    }

    fun getYear() : String? {
        return year.value?: SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
    }

    fun setYear(year : String?) {
        this.year.value = year
    }

    fun getBitmapImage() : Bitmap? {
        return bitmapImage.value
    }

    fun setBitmapImage(bitmapImage: Bitmap?) {
        this.bitmapImage.value = bitmapImage
    }

}