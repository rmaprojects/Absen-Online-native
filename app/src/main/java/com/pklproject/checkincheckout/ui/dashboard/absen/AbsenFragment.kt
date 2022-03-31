package com.pklproject.checkincheckout.ui.dashboard.absen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
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

class AbsenFragment : Fragment(R.layout.fragment_absen) {

    private val binding: FragmentAbsenBinding by viewBinding()
    private val viewModel: ServiceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val absen = arguments?.getString(ABSEN_TYPE)
        val tinyDB = TinyDB(requireContext())

        val cameraResult = viewModel.getResultPicture()
        if (cameraResult == null) {
            binding.hasilfoto.isVisible = false
        } else {
            try {
                cameraResult.toBitmap(210, 450) {
                    binding.hasilfoto.setImageBitmap(it)
                    binding.cauctionTxt.isVisible = false
                }
            } catch (e: UnsupportedOperationException) {
                binding.hasilfoto.isVisible = false
                binding.cauctionTxt.isVisible = true
            }
        }

        initialisation(tinyDB, absen.toString())
    }

    private fun initialisation(tinyDB: TinyDB, absen: String) {

        binding.ambilfoto.setOnClickListener {
            findNavController().navigate(R.id.action_absenFragment_to_cameraView)
        }

        binding.kirimabsen.isEnabled = viewModel.getLatitude() != 0.0 && viewModel.getLongitude() != 0.0

        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val keterangan = binding.keterangan.text

        binding.kirimabsen.setOnClickListener {
            val longitude = viewModel.getLongitude() ?: 0.0
            val latitude = viewModel.getLatitude() ?: 0.0
            kirimAbsen(username!!, password!!, absen, keterangan.toString(), longitude, latitude)
        }
    }

    private fun kirimAbsen(
        username: String,
        password: String,
        tipeAbsen: String,
        keterangan: String,
        longitude: Double,
        latitude: Double
    ) {
        val api = ApiInterface.createApi()
        Log.d("longitude", longitude.toString())
        Log.d("latitude", latitude.toString())
        lifecycleScope.launch {
            try {
                val response = api.kirimAbsen(username, password, tipeAbsen, longitude, latitude, "namaphoto", keterangan)
                Log.d("response", response.toString())
                if (response.code == 200) {
                    viewModel.setResultPicture(null)
                    findNavController().navigateUp()
                    Snackbar.make(
                        requireActivity().findViewById(R.id.container),
                        "Berhasil absen",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        binding.root,
                        "Gagal absen, silahkan coba lagi",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.d("Error", e.toString())
                Snackbar.make(binding.root, "Gagal", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        const val ABSEN_TYPE = "ABSENTYPEKEY"
    }
}