package com.pklproject.checkincheckout.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otaliastudios.cameraview.PictureResult
import java.util.ArrayList

class ServiceViewModel : ViewModel() {

//    private val absenSettings : MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    private val longitude : MutableLiveData<Double> = MutableLiveData<Double>()
    private val latitude : MutableLiveData<Double> = MutableLiveData<Double>()

    private val resultPicture : MutableLiveData<PictureResult> = MutableLiveData<PictureResult>()

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