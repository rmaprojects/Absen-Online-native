package com.pklproject.checkincheckout.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.ArrayList

class ServiceViewModel : ViewModel() {

    private val absenSettings : MutableLiveData<List<String>> = MutableLiveData<List<String>>()

    fun getAbsenSettings() : MutableLiveData<List<String>> {
        return absenSettings
    }

    fun setAbsenSettings(settings : List<String>) {
        absenSettings.value = settings
    }

    fun location(locations: ArrayList<Location>) {
        val latitude = locations.first().latitude
        val longitude = locations.first().longitude

    }


}