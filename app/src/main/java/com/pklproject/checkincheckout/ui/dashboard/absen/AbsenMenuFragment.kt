package com.pklproject.checkincheckout.ui.dashboard.absen

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
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
import com.pklproject.checkincheckout.databinding.FragmentMenuAbsenBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.TinyDB
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
import mumayank.com.airlocationlibrary.AirLocation
import java.text.SimpleDateFormat
import java.util.*

class AbsenMenuFragment : Fragment(R.layout.fragment_menu_absen) {

    private val binding: FragmentMenuAbsenBinding by viewBinding()
    private lateinit var airLocation: AirLocation
    private val viewModel: ServiceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        airLocation = AirLocation(requireActivity(), object : AirLocation.Callback {
            override fun onSuccess(locations: ArrayList<Location>) {
                viewModel.setLatitude(locations[0].latitude)
                viewModel.setLongitude(locations[0].longitude)
                binding.kirim.isEnabled = true
            }

            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
                viewModel.setLatitude(0.0)
                viewModel.setLongitude(0.0)
                binding.kirim.isEnabled = false
                Snackbar.make(binding.root, "Gagal mendapatkan lokasi", Snackbar.LENGTH_SHORT)
                    .show()
            }

        }, true)

        airLocation.start()
        val tinyDB = TinyDB(requireContext())

        initialisation(tinyDB)

        binding.absenpagi.setOnClickListener {
            goToAbsensi("1")
        }

        binding.absensiang.setOnClickListener {
            goToAbsensi("2")
        }

        binding.absenpulang.setOnClickListener {
            goToAbsensi("3")
        }

    }

    private fun initialisation(tinyDB: TinyDB) {
        binding.pilihanAbsen.check(R.id.absen)
        binding.absensi.isVisible = true
        binding.izindialog.isVisible = false

        val api = ApiInterface.createApi()
        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val hariIni = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        cekAbsenToday(api, username.toString(), password.toString(), hariIni)

        val keterangan = binding.keterangan.text

        binding.pilihanAbsen.setOnCheckedChangeListener{ _, isChecked ->
            when(isChecked){
                R.id.absen -> {
                    binding.absensi.isVisible = true
                    binding.izindialog.isVisible = false
                    cekAbsenToday(api, username.toString(), password.toString(), hariIni)
                }
                R.id.izin -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    binding.kirim.setOnClickListener {
                        kirimAbsen("5", tinyDB, keterangan.toString())
                    }
                }
                R.id.cuti -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    binding.kirim.setOnClickListener {
                        kirimAbsen("4", tinyDB, keterangan.toString())
                    }
                }
            }
        }
    }

    private fun kirimAbsen (tipeAbsen: String, tinyDB: TinyDB, keterangan:String) {

        val api = ApiInterface.createApi()
        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val longitude = viewModel.getLongitude()?:0.0
        val latitude = viewModel.getLatitude()?:0.0

        lifecycleScope.launch {
            try {
                val response = api.kirimAbsen(username.toString(), password.toString(), tipeAbsen, longitude, latitude, null, keterangan)
                if (response.code == 200) {
                    Snackbar.make(binding.rootLayout, "Sukses mengirim absen ${response.tipeAbsen}", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                    binding.kirim.isEnabled = false
                } else {
                    Snackbar.make(binding.rootLayout, "Data gagal dikirim", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
                Snackbar.make(binding.rootLayout, "Gagal mengambil data, aktifkan internet anda", Snackbar.LENGTH_SHORT)
                    .setAction("Ok") {}
                    .show()
            }
        }
    }

    private fun cekAbsenToday(api: ApiInterface, username:String, password:String, hariIni:String) {
        lifecycleScope.launch {
            try {
                val response = api.cekAbsenHariIni(username, password, hariIni)
                val statusAbsen = response.absenHariIni
                when (statusAbsen?.get(0)?.tipeAbsen) {
                    "Data Kosong" -> {
                        binding.absenpagi.isClickable = true
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = true
                    }
                    "pagi" -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = true
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = true
                    }
                    "siang" -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = true
                        binding.kirim.isEnabled = true
                    }
                    "pulang" -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = true
                    }
                    "izin" -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = false
                    }
                    "cuti" -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = false
                    }
                    else -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = false
                    }
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
                Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengambil data, aktifkan internet anda", Snackbar.LENGTH_SHORT)
                    .setAction("Ok") {}
                    .show()
            }
        }
    }

    private fun goToAbsensi(tipeAbsen:String) {
        val bundle = bundleOf(AbsenFragment.ABSEN_TYPE to tipeAbsen)
        findNavController().navigate(R.id.action_navigation_absen_to_absenFragment, bundle)
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
}