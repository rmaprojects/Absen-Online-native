package com.pklproject.checkincheckout.ui.dashboard.absen

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentAbsenBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.TinyDB
import kotlinx.coroutines.launch
import mumayank.com.airlocationlibrary.AirLocation

class AbsenFragment : Fragment(R.layout.fragment_absen) {

    private val binding: FragmentAbsenBinding by viewBinding()
    private lateinit var airLocation: AirLocation


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val absen = arguments?.getString(ABSEN_TYPE)

        val tinyDB = TinyDB(requireContext())

        var latitude = 0.0
        var longtude = 0.0

        airLocation = AirLocation(requireActivity(), object : AirLocation.Callback {

            override fun onSuccess(locations: ArrayList<Location>) {
                 latitude = locations.first().latitude
                 longtude = locations.first().longitude
            }

            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {

            }
        },true)
        airLocation.start()
        initialisation(tinyDB, absen.toString(), latitude, longtude)
    }



    private fun initialisation(tinyDB: TinyDB, absen:String, latitude : Double, longtude: Double) {

        val keterangan = binding.keterangan.text

        binding.kirimabsen.setOnClickListener {
            kirimAbsen(absen,tinyDB,keterangan.toString(),latitude,longtude )
        }
    }

    private fun kirimAbsen (tipeAbsen: String, tinyDB: TinyDB, keterangan:String, latitude: Double,  longtude: Double) {
        val api = ApiInterface.createApi()
        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val latitude = latitude
        val longitude = longtude

        lifecycleScope.launch {
            try {
                val response = api.kirimAbsen(username.toString(), password.toString(), tipeAbsen, longitude, latitude, null, keterangan)
                if (response.code == 200) {
                    Snackbar.make(binding.root, "Berhasil Absen", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, "Gagal Absen", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Gagal mengambil data, aktifkan internet anda", Snackbar.LENGTH_SHORT)
                    .setAction("Ok") {}
                    .show()
            }
        }
        Log.d("longitude",longitude.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLocation.onActivityResult(
            requestCode,
            resultCode,
            data
        ) // ADD THIS LINE INSIDE onActivityResult
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        ) // ADD THIS LINE INSIDE onRequestPermissionResult
    }


    companion object {
        const val ABSEN_TYPE = "ABSENTYPEKEY"
    }
}