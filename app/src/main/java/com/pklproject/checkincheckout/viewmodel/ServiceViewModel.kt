package com.pklproject.checkincheckout.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServiceViewModel : ViewModel() {

    private val absenSettings : MutableLiveData<List<String>> = MutableLiveData<List<String>>()

    fun getAbsenSettings() : MutableLiveData<List<String>> {
        return absenSettings
    }

    fun setAbsenSettings(settings : List<String>) {
        absenSettings.value = settings
    }



}