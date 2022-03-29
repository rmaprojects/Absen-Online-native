package com.pklproject.checkincheckout.ui.dashboard.absen

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentAbsenBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.TinyDB
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
import mumayank.com.airlocationlibrary.AirLocation

class AbsenFragment : Fragment(R.layout.fragment_absen) {

    private val binding: FragmentAbsenBinding by viewBinding()
    private lateinit var airLocation: AirLocation
    private val viewModel: ServiceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val absen = arguments?.getString(ABSEN_TYPE)
        val tinyDB = TinyDB(requireContext())

        airLocation = AirLocation(requireActivity(), object:AirLocation.Callback{
            override fun onSuccess(locations: ArrayList<Location>) {
                viewModel.setLatitude(locations[0].latitude)
                viewModel.setLongitude(locations[0].longitude)
                binding.kirimabsen.isEnabled = true
            }

            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
                viewModel.setLatitude(0.0)
                viewModel.setLongitude(0.0)
                binding.kirimabsen.isEnabled = false
                Snackbar.make(binding.root, "Gagal mendapatkan lokasi", Snackbar.LENGTH_SHORT).show()
            }

        }, true)

        airLocation.start()

        initialisation(tinyDB, absen.toString())
    }

    private fun initialisation(tinyDB: TinyDB, absen:String) {

        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val longitude = viewModel.getLongitude()?:0.0
        val latitude = viewModel.getLatitude()?:0.0
        val keterangan = binding.keterangan.text

        binding.kirimabsen.setOnClickListener {
            kirimAbsen(username!!, password!!, absen, keterangan.toString(), longitude, latitude)
        }
    }

    private fun kirimAbsen(username:String, password:String, tipeAbsen:String, keterangan:String, longitude:Double, latitude:Double) {
        val api = ApiInterface.createApi()
        Log.d("longitude", longitude.toString())
        Log.d("latitude", latitude.toString())
        lifecycleScope.launch {
            try {
                val response = api.kirimAbsen(username, password, tipeAbsen, longitude, latitude, "namaphoto", keterangan)
                if (response.code == 200) {
                    findNavController().navigateUp()
                    Snackbar.make(requireActivity().findViewById(R.id.container), "Berhasil absen", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, "Gagal absen, silahkan coba lagi", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.d("Error", e.toString())
                Snackbar.make(binding.root, "Gagal", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLocation.onActivityResult(
            requestCode,
            resultCode,
            data
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        airLocation.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }


    companion object {
        const val ABSEN_TYPE = "ABSENTYPEKEY"
    }
}