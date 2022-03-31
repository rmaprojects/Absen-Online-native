package com.pklproject.checkincheckout.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otaliastudios.cameraview.PictureResult
import com.pklproject.checkincheckout.api.models.AbsenHariIni

class ServiceViewModel : ViewModel() {

    private val longitude : MutableLiveData<Double> = MutableLiveData<Double>()
    private val latitude : MutableLiveData<Double> = MutableLiveData<Double>()

    private val resultPicture : MutableLiveData<PictureResult> = MutableLiveData<PictureResult>()
    private val todayAttendance : MutableLiveData<List<AbsenHariIni>> = MutableLiveData<List<AbsenHariIni>>()

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

}