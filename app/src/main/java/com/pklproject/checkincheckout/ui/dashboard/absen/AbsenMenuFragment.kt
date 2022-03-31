package com.pklproject.checkincheckout.ui.dashboard.absen

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.location.LocationManagerCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.MainActivity
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.api.models.TodayAttendanceModel
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

    override fun onStart() {
        super.onStart()
        if (isLocationEnabled(requireContext())) {
            airLocation.start()
        } else {
            Snackbar.make(binding.root, "Aktifkan lokasi terlebih dahulu", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

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
            when(isChecked) {
                R.id.absen -> {
                    binding.absensi.isVisible = true
                    binding.izindialog.isVisible = false
                    cekAbsenToday(api, username.toString(), password.toString(), hariIni)
                }
                R.id.izin -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    binding.kirim.setOnClickListener {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Yakin ingin izin?")
                            .setMessage("Setelah anda klik 'Ya', maka anda tidak akan bisa absen lagi, lanjutkan?")
                            .setNegativeButton("Tidak") { _, _ -> }
                            .setPositiveButton("Ya") { _,_ ->
                                kirimAbsen("5", tinyDB, keterangan.toString())
                            }
                            .show()
                    }
                }
                R.id.cuti -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    binding.kirim.setOnClickListener {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Yakin ingin izin?")
                            .setMessage("Setelah anda klik 'Ya', maka anda tidak akan bisa absen lagi, lanjutkan?")
                            .setNegativeButton("Tidak") { _, _ -> }
                            .setPositiveButton("Ya") { _, _ ->
                                kirimAbsen("4", tinyDB, keterangan.toString())
                            }
                            .show()
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
                var txtJamAbsenPagi = ""
                var txtJamAbsenSiang = ""
                var txtJamAbsenPulang = ""
                var txtStatusAbsenPagi = ""
                var txtStatusAbsenSiang = ""
                var txtStatusAbsenPulang = ""
                val statusImageDay = binding.statusIconDay
                val statusImageSiang = binding.statusIconNoon
                val statusImagePulang = binding.statusIconPulang

                if (response.code == 200) {
                    viewModel.setTodayAttendance(response.absenHariIni)
                } else {
                    viewModel.setTodayAttendance(null)
                }
                when (statusAbsen?.get(0)?.tipeAbsen) {
                    "Data Kosong" -> {
                        binding.kirim.isEnabled = true
                        txtJamAbsenPagi = "--:--"
                        txtStatusAbsenPagi = "Belum Absen"
                        statusImageDay.setImageResource(R.drawable.ic_baseline_not_available_24)
                        txtJamAbsenSiang = "--:--"
                        txtStatusAbsenSiang = "Belum Tersedia"
                        statusImageSiang.isVisible = false
                        txtJamAbsenPulang = "--:--"
                        txtStatusAbsenPulang = "Belum Tersedia"
                        statusImagePulang.isVisible = false

                        binding.absenpagi.setOnClickListener {
                            goToAbsensi("1")
                        }

                        binding.absensiang.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda masih belum bisa melakukan absen siang", Toast.LENGTH_SHORT).show()
                        }

                        binding.absenpulang.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda masih belum bisa melakukan absen pulang", Toast.LENGTH_SHORT).show()
                        }
                    }
                    "pagi" -> {
                        binding.kirim.isEnabled = true
                        txtJamAbsenPagi = viewModel.getTodayAttendance()?.get(0)?.waktuAbsen ?: "--:--"
                        txtStatusAbsenPagi = "Sudah Absen"
                        statusImageDay.setImageResource(R.drawable.ic_sudah_absen)
                        txtStatusAbsenSiang = "Belum Absen"
                        statusImageSiang.setImageResource(R.drawable.ic_baseline_not_available_24)
                        txtStatusAbsenPulang = "Belum Tersedia"
                        statusImagePulang.isVisible = false

                        binding.absenpagi.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah absen pagi, silahkan absen siang", Toast.LENGTH_SHORT).show()
                        }

                        binding.absensiang.setOnClickListener {
                            goToAbsensi("2")
                        }

                        binding.absenpulang.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda belum bisa melakukan absen pulang", Toast.LENGTH_SHORT).show()
                        }
                    }
                    "siang" -> {
                        //ketika datanya pertama siang, bisanya absen pulang doang, pagi dan siang g bisa
                        binding.kirim.isEnabled = true
                        txtJamAbsenSiang = TinyDB(requireContext()).getObject(MainActivity.PENGATURANABSENKEY,TodayAttendanceModel::class.java).absenHariIni?.get(0)?.waktuAbsen.toString()
                        txtStatusAbsenPagi = "Sudah Absen"
                        statusImageDay.setImageResource(R.drawable.ic_sudah_absen)
                        txtStatusAbsenSiang = "Sudah Absen"
                        statusImageSiang.setImageResource(R.drawable.ic_sudah_absen)
                        txtStatusAbsenPulang = "Belum Absen"
                        statusImagePulang.setImageResource(R.drawable.ic_baseline_not_available_24)

                        binding.absenpagi.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen pagi", Toast.LENGTH_SHORT).show()
                        }

                        binding.absensiang.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen siang", Toast.LENGTH_SHORT).show()
                        }

                        binding.absenpulang.setOnClickListener {
                            goToAbsensi("3")
                        }

                    }
                    "pulang" -> {
                        binding.kirim.isEnabled = false
                        txtJamAbsenPulang = TinyDB(requireContext()).getObject(MainActivity.PENGATURANABSENKEY, TodayAttendanceModel::class.java).absenHariIni?.get(0)?.waktuAbsen.toString()
                        txtStatusAbsenPagi = "Sudah Absen"
                        statusImageDay.setImageResource(R.drawable.ic_sudah_absen)
                        txtStatusAbsenSiang = "Sudah Absen"
                        statusImageSiang.setImageResource(R.drawable.ic_sudah_absen)
                        txtStatusAbsenPulang = "Sudah Absen"
                        statusImagePulang.setImageResource(R.drawable.ic_sudah_absen)

                        binding.absenpagi.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen pagi", Toast.LENGTH_SHORT).show()
                        }

                        binding.absensiang.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen siang", Toast.LENGTH_SHORT).show()
                        }

                        binding.absenpulang.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen pulang", Toast.LENGTH_SHORT).show()
                        }
                    }
                    "izin" -> {
                        binding.kirim.isEnabled = false
                        binding.absenpagi.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen cuti/izin", Toast.LENGTH_SHORT).show()
                        }

                        binding.absensiang.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen cuti/izin", Toast.LENGTH_SHORT).show()
                        }

                        binding.absenpulang.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen cuti/izin", Toast.LENGTH_SHORT).show()
                        }
                    }
                    "cuti" -> {
                        binding.absenpagi.isClickable = false
                        binding.absenpagi.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen cuti/izin", Toast.LENGTH_SHORT).show()
                        }

                        binding.absensiang.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen cuti/izin", Toast.LENGTH_SHORT).show()
                        }

                        binding.absenpulang.setOnClickListener {
                            Toast.makeText(requireContext(), "Anda sudah melakukan absen cuti/izin", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {
                        binding.absenpagi.isClickable = false
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                        binding.kirim.isEnabled = false
                    }
                }

                binding.txtJamAbsenDay.text = txtJamAbsenPagi
                binding.txtJamAbsenNoon.text = txtJamAbsenSiang
                binding.txtJamAbsenPulang.text = txtJamAbsenPulang
                binding.txtStatusDay.text = txtStatusAbsenPagi
                binding.txtStatusNoon.text = txtStatusAbsenSiang
                binding.txtStatusPulang.text = txtStatusAbsenPulang

            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Terjadi Kesalahan")
                    .setMessage("Gagal mengambil data, aktifkan internet anda, atau cobalah untuk membuka ulang aplikasi")
                    .setPositiveButton("Ok") { _, _ -> }
                    .create().show()
                binding.absenpagi.isClickable = false
                binding.absensiang.isClickable = false
                binding.absenpulang.isClickable = false
                binding.kirim.isEnabled = false
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

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }
}