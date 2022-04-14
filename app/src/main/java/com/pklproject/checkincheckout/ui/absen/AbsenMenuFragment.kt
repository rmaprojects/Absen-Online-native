package com.pklproject.checkincheckout.ui.absen

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.pklproject.checkincheckout.api.models.Setting
import com.pklproject.checkincheckout.databinding.FragmentMenuAbsenBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.ui.settings.SettingsFragment
import com.pklproject.checkincheckout.ui.settings.TinyDB
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
import mumayank.com.airlocationlibrary.AirLocation
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
        setTextAppearance(requireContext())
    }

    private fun setTextAppearance(context: Context) {
        val appearanceSettings = Preferences(context).textSize
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            when (appearanceSettings) {
//                "kecil" -> {
//                    binding.AbsenPagi.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
//                    binding.Absensiang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
//                    binding.Absenpulang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
//                }
//                "normal" -> {
//                    binding.AbsenPagi.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
//                    binding.Absensiang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
//                    binding.Absenpulang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
//                }
//                "besar" -> {
//                    binding.AbsenPagi.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Display1)
//                    binding.Absensiang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Display1)
//                    binding.Absenpulang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Display1)
//                }
            }
        } else {
            when (appearanceSettings) {
                "kecil" -> {
//                    binding.ubahfontSlider.value = 0F
                    binding.AbsenPagi.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.Absensiang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.Absenpulang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                }
                "normal" -> {
//                    binding.ubahfontSlider.value = 1F
                    binding.AbsenPagi.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
                    binding.Absensiang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
                    binding.Absenpulang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
                }
                "besar" -> {
//                    binding.ubahfontSlider.value = 2F
                    binding.AbsenPagi.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Display1)
                    binding.Absensiang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Display1)
                    binding.Absenpulang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Display1)
                }
            }
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
        val settingsAbsen = tinyDB.getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java)

        cekAbsenToday(api, username.toString(), password.toString(), hariIni, settingsAbsen)

        val keterangan = binding.keterangan.text

        binding.pilihanAbsen.setOnCheckedChangeListener{ _, isChecked ->
            when(isChecked) {
                R.id.absen -> {
                    binding.absensi.isVisible = true
                    binding.izindialog.isVisible = false
                    cekAbsenToday(api, username.toString(), password.toString(), hariIni, settingsAbsen)
                }
                R.id.izin -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    binding.kirim.setOnClickListener {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Yakin ingin izin?")
                            .setMessage("Setelah anda klik 'Ya', maka anda tidak akan bisa absen lagi, lanjutkan?")
                            .setNegativeButton("Tidak") { _, _ -> }
                            .setPositiveButton("Ya") { _, _ ->
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
                            .setTitle("Yakin ingin cuti?")
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
        var jenisAbsen = ""

        if (tipeAbsen == "4") {
            jenisAbsen = "Cuti"
        } else if (tipeAbsen == "5") {
            jenisAbsen = "Izin"
        }

        val api = ApiInterface.createApi()
        val username = convertToRequstBody(tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username.toString())
        val password = convertToRequstBody(tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password.toString())
        val hariIni = convertToRequstBody(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
        val jamSekarang = convertToRequstBody(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()))
        val longitude = convertToRequstBody(viewModel.getLongitude().toString())
        val latitude = convertToRequstBody(viewModel.getLatitude().toString())
        val absenType = convertToRequstBody(tipeAbsen)
        val catatan = convertToRequstBody(keterangan)
        val idAbsensi = convertToRequstBody(tinyDB.getString(AbsenFragment.KEYIDABSEN))

        lifecycleScope.launch {
            try {
                val response = api.kirimAbsen(username, password, absenType, longitude, latitude, null, catatan, jamSekarang, idAbsensi, hariIni)
                if (response.body()?.status == true) {
                    Snackbar.make(binding.root, "Berhasil mengajukan $jenisAbsen", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                    binding.kirim.isEnabled = false
                    binding.absenpagi.isClickable = false
                    binding.absensiang.isClickable = false
                    binding.absenpulang.isClickable = false
                } else {
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.d("error", e.toString())
                Snackbar.make(binding.root, "Gagal mengirim absen, coba lagi atau periksa internet anda", Snackbar.LENGTH_SHORT)
                    .setAction("Ok") {}
                    .show()
            }
        }
    }

    private fun cekAbsenToday(api: ApiInterface, username:String, password:String, hariIni:String, settingsAbsen:Setting) {
        lifecycleScope.launch {
            try {
                val responseCatch = api.cekAbsenHariIni(username, password, hariIni)
                val listJamMasuk = responseCatch.absenHariIni?.get(0)
                var txtJamAbsenPagi = ""
                var txtJamAbsenSiang = ""
                var txtJamAbsenPulang = ""
                var txtStatusAbsenPagi = ""
                var txtStatusAbsenSiang = ""
                var txtStatusAbsenPulang = ""
                val statusImageDay = binding.statusIconDay
                val statusImageSiang = binding.statusIconNoon
                val statusImagePulang = binding.statusIconPulang

                if (responseCatch.code == 200) {
                    viewModel.setTodayAttendance(responseCatch.absenHariIni)
                } else {
                    viewModel.setTodayAttendance(null)
                }
                Log.d("absen dibutuhkan", listJamMasuk?.absenYangDibutuhkan.toString())
                when (listJamMasuk?.absenYangDibutuhkan) {
                    null -> {
                        binding.kirim.isEnabled = true
                        txtJamAbsenPagi = "--:--"
                        txtStatusAbsenPagi = if (checkIfAttendanceIsLate("pagi", settingsAbsen)) {
                            statusImageDay.setImageResource(R.drawable.ic_telat)
                            "Terlambat"
                        } else {
                            statusImageDay.setImageResource(R.drawable.ic_baseline_not_available_24)
                            "Belum Absen"
                        }
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
                    "siang" -> {
                        binding.kirim.isEnabled = true
                        txtJamAbsenPagi = viewModel.getTodayAttendance()?.get(0)?.jamMasukPagi ?: "--:--"
                        txtJamAbsenSiang = "--:--"
                        txtJamAbsenPulang = "--:--"
                        txtStatusAbsenPagi = "Sudah Absen"
                        statusImageDay.setImageResource(R.drawable.ic_sudah_absen)
                        if (checkIfAttendanceIsLate("siang", settingsAbsen)) {
                            txtStatusAbsenSiang = "Terlambat"
                            statusImageSiang.setImageResource(R.drawable.ic_telat)
                        } else {
                            txtStatusAbsenSiang = "Belum Absen"
                            statusImageSiang.setImageResource(R.drawable.ic_baseline_not_available_24)
                        }
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
                    "pulang" -> {
                        binding.kirim.isEnabled = true
                        txtJamAbsenSiang = viewModel.getTodayAttendance()?.get(0)?.jamMasukSiang ?: "--:--"
                        txtJamAbsenPagi = viewModel.getTodayAttendance()?.get(0)?.jamMasukPagi ?: "--:--"
                        txtJamAbsenPulang = "--:--"
                        txtStatusAbsenPagi = "Sudah Absen"
                        statusImageDay.setImageResource(R.drawable.ic_sudah_absen)
                        txtStatusAbsenSiang = "Sudah Absen"
                        statusImageSiang.setImageResource(R.drawable.ic_sudah_absen)
                        if (checkIfAttendanceIsLate("pulang", settingsAbsen)) {
                            txtStatusAbsenPulang = "Terlambat"
                            statusImagePulang.setImageResource(R.drawable.ic_telat)
                        } else {
                            txtStatusAbsenPulang = "Belum Absen"
                            statusImagePulang.setImageResource(R.drawable.ic_baseline_not_available_24)
                        }

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
                    "selesai" -> {
                        binding.kirim.isEnabled = false
                        txtJamAbsenPagi = viewModel.getTodayAttendance()?.get(0)?.jamMasukPagi ?: "--:--"
                        txtJamAbsenSiang = viewModel.getTodayAttendance()?.get(0)?.jamMasukSiang ?: "--:--"
                        txtJamAbsenPulang = viewModel.getTodayAttendance()?.get(0)?.jamMasukPulang ?: "--:--"
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

    private fun checkIfAttendanceIsLate(eventType:String, absenSetings:Setting) : Boolean {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val waktuPagiAkhir = sdf.parse(absenSetings.absenPagiAkhir)
        val waktuSiangAkhir = sdf.parse(absenSetings.absenSiangAkhir)
        val waktuPulangAkhir = sdf.parse(absenSetings.absenPulangAkhir)

        return when (eventType) {
            "pagi" -> {
                val timeNow = sdf.parse(sdf.format(Date()))
                timeNow!!.after(waktuPagiAkhir)
            }
            "siang" -> {
                val timeNow = sdf.parse(sdf.format(Date()))
                timeNow!!.after(waktuSiangAkhir)
            }
            "pulang" -> {
                val timeNow = sdf.parse(sdf.format(Date()))
                timeNow!!.after(waktuPulangAkhir)
            }
            else -> false
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

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    private fun convertToRequstBody(value:String) : RequestBody {
        return  value.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}