package com.pklproject.checkincheckout.ui.absen

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chibatching.kotpref.Kotpref
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.preferencesmodel.AbsenSettingsPreferences
import com.pklproject.checkincheckout.api.models.preferencesmodel.LoginPreferences
import com.pklproject.checkincheckout.databinding.FragmentMenuAbsenBinding
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.*

class AbsenMenuFragment : Fragment(R.layout.fragment_menu_absen) {

    private val binding: FragmentMenuAbsenBinding by viewBinding()
    private val viewModel: ServiceViewModel by activityViewModels()

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Kotpref.init(requireContext())
        val username = LoginPreferences.username
        val password = LoginPreferences.password
        val hariIni = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val settingsAbsen = AbsenSettingsPreferences
        binding.pilihanAbsen.check(R.id.absen)
        binding.izin.isVisible = false
        binding.cuti.isVisible = false
        binding.absensi.isVisible = false
        binding.layoutIzinTxt.isVisible = true
        binding.cutiHariIniText.text = "Loading..."
        binding.izindialog.isVisible = false
        cekAbsenTodayApi(username, password, hariIni, settingsAbsen)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialisation()
        setTextAppearance(requireContext())
    }

    private fun initialisation() {

        binding.kirim.isEnabled = !(viewModel.getLatitude() == 0.0 && viewModel.getLongitude() == 0.0)

        val settingsAbsen = AbsenSettingsPreferences

        val keterangan = binding.keterangan.text

        val username = LoginPreferences.username
        val password = LoginPreferences.password
        val hariIni = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        binding.pilihanAbsen.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                R.id.absen -> {
                    binding.absensi.isVisible = true
                    binding.izindialog.isVisible = false
                    cekAbsenTodayApi(username, password, hariIni, settingsAbsen)
                }
                R.id.izin -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    binding.kirim.setOnClickListener {
                        if (viewModel.getServerClock() == null) {
                            Snackbar.make(requireView(), "Jam server gagal terambil, silahakn buka ulang aplikasi", Snackbar.LENGTH_LONG).show()
                        } else {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Yakin ingin izin?")
                                .setMessage("Setelah anda klik 'Ya', maka anda tidak akan bisa absen lagi, lanjutkan?")
                                .setNegativeButton("Tidak") { _, _ -> }
                                .setPositiveButton("Ya") { _, _ ->
                                    kirimAbsen("5", keterangan.toString())
                                }
                                .show()
                        }
                    }
                }
                R.id.cuti -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    binding.kirim.setOnClickListener {
                        if (viewModel.getServerClock() == null) {
                            Snackbar.make(requireView(), "Jam server gagal terambil, silahakn buka ulang aplikasi", Snackbar.LENGTH_LONG).show()
                        } else {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Yakin ingin cuti?")
                                .setMessage("Setelah anda klik 'Ya', maka anda tidak akan bisa absen lagi, lanjutkan?")
                                .setNegativeButton("Tidak") { _, _ -> }
                                .setPositiveButton("Ya") { _, _ ->
                                    kirimAbsen("4", keterangan.toString())
                                }
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun kirimAbsen(tipeAbsen: String, keterangan: String) {
        var jenisAbsen = ""

        if (tipeAbsen == "4") {
            jenisAbsen = "Cuti"
        } else if (tipeAbsen == "5") {
            jenisAbsen = "Izin"
        }
        val api = ApiInterface.createApi()
        val username = convertToRequstBody(
            LoginPreferences.username
        )
        val password = convertToRequstBody(
            LoginPreferences.password
        )
        val isTelat = convertToRequstBody("0")
        val hariIni =
            convertToRequstBody(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
        val jamSekarang =
            convertToRequstBody(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()))
        val longitude = convertToRequstBody(viewModel.getLongitude().toString())
        val latitude = convertToRequstBody(viewModel.getLatitude().toString())
        val absenType = convertToRequstBody(tipeAbsen)
        val catatan = convertToRequstBody(keterangan)

        lifecycleScope.launch {
            try {
                val response = api.kirimAbsen(username, password, absenType, longitude, latitude, null, catatan, jamSekarang, hariIni, isTelat)
                if (response.isSuccessful) {
                    Log.d("response", response.toString())
                    if (response.body()?.status == true) {
                        Snackbar.make(
                            requireActivity().findViewById(R.id.container),
                            "Berhasil mengajukan $jenisAbsen",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction("Ok") {}
                            .show()
                        Log.d("kirimAbsen", response.body()!!.status.toString())
                        Log.d("tipe absen", response.body()!!.tipeAbsen.toString())
                        binding.izin.isVisible = false
                        binding.cuti.isVisible = false
                        binding.absensi.isVisible = false
                        binding.layoutIzinTxt.isVisible = true
                        binding.cutiHariIniText.text = "Anda sudah izin/absen hari ini, tidak perlu absen lagi"
                        binding.izindialog.isVisible = false
                        binding.pilihanAbsen.isVisible = false
                        binding.divider.isVisible = false
                    } else {
                        Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT)
                        .show()
                    Log.d("response", response.toString())
                }
            } catch (e: Exception) {
                Log.d("error", e.toString())
                Snackbar.make(
                    binding.root,
                    "Gagal mengirim absen, coba lagi atau periksa internet anda",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("Ok") {}
                    .show()
            }
        }
    }

    private fun cekAbsenTodayApi(username:String, password:String, hariIni:String, settingsAbsen: AbsenSettingsPreferences) {
        val api = ApiInterface.createApi()
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

                val waktuAbsenPagi = settingsAbsen.absenPagiAwal
                val splittedWaktuAbsenPagi = waktuAbsenPagi.split(":")
                val jamAbsenPagi = Calendar.getInstance()
                jamAbsenPagi.set(Calendar.HOUR_OF_DAY, splittedWaktuAbsenPagi[0].toInt())
                jamAbsenPagi.set(Calendar.MINUTE, splittedWaktuAbsenPagi[1].toInt())
                jamAbsenPagi.set(Calendar.SECOND, splittedWaktuAbsenPagi[2].toInt())

                val waktuAbsenSiang = settingsAbsen.absenSiangAwal
                val splittedWaktuAbsenSiang = waktuAbsenSiang.split(":")
                val jamAbsenSiang = Calendar.getInstance()
                jamAbsenSiang.set(Calendar.HOUR_OF_DAY, splittedWaktuAbsenSiang[0].toInt())
                jamAbsenSiang.set(Calendar.MINUTE, splittedWaktuAbsenSiang[1].toInt())
                jamAbsenSiang.set(Calendar.SECOND, splittedWaktuAbsenSiang[2].toInt())

                val waktuAbsenPulang = settingsAbsen.absenPulangAwal
                val splittedWaktuAbsenPulang = waktuAbsenPulang.split(":")
                val jamAbsenPulang = Calendar.getInstance()
                jamAbsenPulang.set(Calendar.HOUR_OF_DAY, splittedWaktuAbsenPulang[0].toInt())
                jamAbsenPulang.set(Calendar.MINUTE, splittedWaktuAbsenPulang[1].toInt())
                jamAbsenPulang.set(Calendar.SECOND, splittedWaktuAbsenPulang[2].toInt())

                val timeNow = Calendar.getInstance()

                Log.d("absen dibutuhkan API", listJamMasuk?.absenYangDibutuhkan.toString())
                Log.d("siangdiperlukanAPI", listJamMasuk?.absenSiangDiperlukan.toString())

                when (listJamMasuk?.absenYangDibutuhkan) {
                    null -> {
                        binding.izin.isVisible = false
                        binding.cuti.isVisible = false
                        binding.absensi.isVisible = false
                        binding.layoutIzinTxt.isVisible = false
                        binding.izindialog.isVisible = false
                    }
                    "pagi" -> {
                        binding.kirim.isEnabled = true
                        binding.izin.isVisible = true
                        binding.cuti.isVisible = true

                        binding.cutiHariIniText.isVisible = false
                        binding.absensi.visibility = View.VISIBLE
                        binding.pilihanAbsen.isVisible = true

                        if (listJamMasuk.absenSiangDiperlukan == "1") {
                            binding.absenSiang.isVisible = true
                        } else if (listJamMasuk.absenSiangDiperlukan == "0") {
                            binding.absenSiang.isVisible = false
                        }

                        binding.layoutIzinTxt.isVisible = false

                        txtJamAbsenPagi = "--:--"
                        txtStatusAbsenPagi = if (checkIfAttendanceIsLate("pagi", settingsAbsen)) {
                            statusImageDay.setImageResource(R.drawable.ic_telat)
                            "Belum Absen"
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

                        binding.absenPagi.setOnClickListener {
                            if (timeNow.timeInMillis < jamAbsenPagi.timeInMillis) {
                                Snackbar.make(
                                    binding.root,
                                    "Absen pagi belum tersedia, silahkan tunggu sampai jam ${settingsAbsen.absenPagiAwal}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                goToAbsensi("1")
                            }
                        }

                        binding.absenSiang.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Anda masih belum bisa melakukan absen siang",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        binding.absenPulang.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Anda masih belum bisa melakukan absen pulang",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    "siang" -> {
                        binding.kirim.isEnabled = true
                        binding.izin.isVisible = false
                        binding.cuti.isVisible = false

                        binding.layoutIzinTxt.isVisible = false
                        binding.absensi.isVisible = true

                        txtJamAbsenPagi = viewModel.getTodayAttendance()?.get(0)?.jamMasukPagi ?: "--:--"
                        txtJamAbsenSiang = "--:--"
                        txtJamAbsenPulang = "--:--"
                        txtStatusAbsenPagi = "Sudah Absen"
                        statusImageDay.setImageResource(R.drawable.ic_sudah_absen)

                        if (checkIfAttendanceIsLate("siang", settingsAbsen)) {
                            txtStatusAbsenSiang = "Belum Absen"
                            statusImageSiang.setImageResource(R.drawable.ic_telat)
                        } else {
                            txtStatusAbsenSiang = "Belum Absen"
                            statusImageSiang.setImageResource(R.drawable.ic_baseline_not_available_24)
                        }
                        txtStatusAbsenPulang = "Belum Tersedia"
                        statusImagePulang.isVisible = false

                        binding.absenPagi.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Anda sudah absen pagi, silahkan absen siang",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        binding.absenSiang.setOnClickListener {
                            if (timeNow.timeInMillis < jamAbsenSiang.timeInMillis) {
                                Snackbar.make(
                                    binding.root,
                                    "Absen siang belum tersedia, silahkan tunggu sampai jam ${settingsAbsen.absenSiangAwal}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                goToAbsensi("2")
                            }
                        }

                        binding.absenPulang.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Anda belum bisa melakukan absen pulang",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    "pulang-siang-tidak-perlu" -> {
                        binding.kirim.isEnabled = true
                        binding.izin.isVisible = false
                        binding.cuti.isVisible = false

                        binding.layoutIzinTxt.isVisible = false
                        binding.absensi.isVisible = true

                        txtJamAbsenSiang = viewModel.getTodayAttendance()?.get(0)?.jamMasukSiang ?: "--:--"
                        txtJamAbsenPagi = viewModel.getTodayAttendance()?.get(0)?.jamMasukPagi ?: "--:--"
                        binding.absenSiang.isVisible = false
                        txtJamAbsenPulang = "--:--"
                        txtStatusAbsenPagi = "Sudah Absen"
                        statusImageDay.setImageResource(R.drawable.ic_sudah_absen)

                        if (checkIfAttendanceIsLate("pulang", settingsAbsen)) {
                            txtStatusAbsenPulang = "Belum Absen"
                            statusImagePulang.setImageResource(R.drawable.ic_telat)
                        } else {
                            txtStatusAbsenPulang = "Belum Absen"
                            statusImagePulang.setImageResource(R.drawable.ic_baseline_not_available_24)
                        }

                        binding.absenPagi.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Anda sudah melakukan absen pagi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        binding.absenPulang.setOnClickListener {
                            if (timeNow.timeInMillis < jamAbsenPulang.timeInMillis) {
                                Snackbar.make(
                                    binding.root,
                                    "Absen pulang belum tersedia, silahkan tunggu sampai jam ${settingsAbsen.absenPulangAwal}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                goToAbsensi("3")
                            }
                        }
                    }
                    "pulang" -> {
                        binding.kirim.isEnabled = true
                        binding.izin.isVisible = false
                        binding.cuti.isVisible = false

                        binding.layoutIzinTxt.isVisible = false
                        binding.absensi.isVisible = true

                        txtJamAbsenSiang = viewModel.getTodayAttendance()?.get(0)?.jamMasukSiang ?: "--:--"
                        txtJamAbsenPagi = viewModel.getTodayAttendance()?.get(0)?.jamMasukPagi ?: "--:--"
                        txtJamAbsenPulang = "--:--"
                        txtStatusAbsenPagi = "Sudah Absen"
                        statusImageDay.setImageResource(R.drawable.ic_sudah_absen)
                        txtStatusAbsenSiang = "Sudah Absen"
                        statusImageSiang.setImageResource(R.drawable.ic_sudah_absen)
                        if (checkIfAttendanceIsLate("pulang", settingsAbsen)) {
                            txtStatusAbsenPulang = "Belum Absen"
                            statusImagePulang.setImageResource(R.drawable.ic_telat)
                        } else {
                            txtStatusAbsenPulang = "Belum Absen"
                            statusImagePulang.setImageResource(R.drawable.ic_baseline_not_available_24)
                        }

                        binding.absenPagi.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Anda sudah melakukan absen pagi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        binding.absenPulang.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Anda sudah melakukan absen siang",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        binding.absenPulang.setOnClickListener {
                            if (timeNow.timeInMillis < jamAbsenPulang.timeInMillis) {
                                Snackbar.make(
                                    binding.root,
                                    "Absen pulang belum tersedia, silahkan tunggu sampai jam ${settingsAbsen.absenPulangAwal}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                goToAbsensi("3")
                            }
                        }
                    }
                    "selesai" -> {
                        binding.kirim.isEnabled = false
                        binding.cuti.isVisible = false
                        binding.izin.isVisible = false

                        if (listJamMasuk.absenSiangDiperlukan == "1") {
                            binding.absenSiang.isVisible = true
                        } else if (listJamMasuk.absenSiangDiperlukan == "0") {
                            binding.absenSiang.isVisible = false
                        }

                        binding.layoutIzinTxt.isVisible = false
                        binding.absensi.isVisible = true

                        txtJamAbsenPagi = viewModel.getTodayAttendance()?.get(0)?.jamMasukPagi ?: "--:--"
                        txtJamAbsenSiang = viewModel.getTodayAttendance()?.get(0)?.jamMasukSiang ?: "--:--"
                        txtJamAbsenPulang = viewModel.getTodayAttendance()?.get(0)?.jamMasukPulang ?: "--:--"
                        txtStatusAbsenPagi = "Sudah Absen"
                        statusImageDay.setImageResource(R.drawable.ic_sudah_absen)
                        txtStatusAbsenSiang = "Sudah Absen"
                        statusImageSiang.setImageResource(R.drawable.ic_sudah_absen)
                        txtStatusAbsenPulang = "Sudah Absen"
                        statusImagePulang.setImageResource(R.drawable.ic_sudah_absen)

                        binding.absenPagi.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Anda sudah melakukan absen pagi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        binding.absenSiang.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Anda sudah melakukan absen siang",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        binding.absenPulang.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Anda sudah melakukan absen pulang",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    "selesai-cuti-atau-izin" -> {
                        binding.izin.isVisible = false
                        binding.cuti.isVisible = false
                        binding.absensi.isVisible = false
                        binding.layoutIzinTxt.isVisible = true
                        binding.cutiHariIniText.text = "Anda sudah izin/absen hari ini, tidak perlu absen lagi"
                        binding.izindialog.isVisible = false
                        binding.pilihanAbsen.isVisible = false
                        binding.divider.isVisible = false
                    }
                    else -> {
                        binding.izin.isVisible = false
                        binding.cuti.isVisible = false
                        binding.absensi.isVisible = false
                        binding.layoutIzinTxt.isVisible = false
                        binding.izindialog.isVisible = false
                    }
                }

                binding.txtJamAbsenDay.text = txtJamAbsenPagi
                binding.txtJamAbsenNoon.text = txtJamAbsenSiang
                binding.txtJamAbsenPulang.text = txtJamAbsenPulang
                binding.txtStatusDay.text = txtStatusAbsenPagi
                binding.txtStatusNoon.text = txtStatusAbsenSiang
                binding.txtStatusPulang.text = txtStatusAbsenPulang

            } catch (e:Exception) {

            }
        }
    }

    private fun goToAbsensi(tipeAbsen: String) {
        val bundle = bundleOf(AbsenFragment.ABSEN_TYPE to tipeAbsen)
        findNavController().navigate(R.id.action_navigation_absen_to_absenFragment, bundle)
    }

    private fun checkIfAttendanceIsLate(eventType: String, absenSetings: AbsenSettingsPreferences): Boolean {
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

    private fun convertToRequstBody(value: String): RequestBody {
        return value.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    private fun setTextAppearance(context: Context) {
        val appearanceSettings = Preferences(context).textSize
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.txtAbsenPagi.setTextAppearance(
                        requireContext(),
                        com.google.android.material.R.style.TextAppearance_AppCompat_Body1
                    )
                    binding.txtAbsenSiang.setTextAppearance(
                        requireContext(),
                        com.google.android.material.R.style.TextAppearance_AppCompat_Body1
                    )
                    binding.txtAbsenPulang.setTextAppearance(
                        requireContext(),
                        com.google.android.material.R.style.TextAppearance_AppCompat_Body1
                    )
                }
                "normal" -> {
                    binding.txtAbsenPagi.setTextAppearance(
                        requireContext(),
                        com.google.android.material.R.style.TextAppearance_AppCompat_Large
                    )
                    binding.txtAbsenSiang.setTextAppearance(
                        requireContext(),
                        com.google.android.material.R.style.TextAppearance_AppCompat_Large
                    )
                    binding.txtAbsenPulang.setTextAppearance(
                        requireContext(),
                        com.google.android.material.R.style.TextAppearance_AppCompat_Large
                    )
                }
                "besar" -> {
                    binding.txtAbsenPagi.setTextAppearance(
                        requireContext(),
                        com.google.android.material.R.style.TextAppearance_AppCompat_Display1
                    )
                    binding.txtAbsenSiang.setTextAppearance(
                        requireContext(),
                        com.google.android.material.R.style.TextAppearance_AppCompat_Display1
                    )
                    binding.txtAbsenPulang.setTextAppearance(
                        requireContext(),
                        com.google.android.material.R.style.TextAppearance_AppCompat_Display1
                    )
                }
            }
        } else {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.txtAbsenPagi.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.txtAbsenSiang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.txtAbsenPulang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                }
                "normal" -> {
                    binding.txtAbsenPagi.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
                    binding.txtAbsenSiang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
                    binding.txtAbsenPulang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
                }
                "besar" -> {
                    binding.txtAbsenPagi.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Display1)
                    binding.txtAbsenSiang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Display1)
                    binding.txtAbsenPulang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Display1)
                }
            }
        }
    }
}