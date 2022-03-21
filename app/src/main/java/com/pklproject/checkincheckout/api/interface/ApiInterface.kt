package com.pklproject.checkincheckout.api.`interface`

import retrofit2.http.Query

interface ApiInterface {

    @Query("SELECT * History")

    @Query("SELECT * AbsenModel")

    @Query("SELECT * Login")
}