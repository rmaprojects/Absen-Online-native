package com.pklproject.checkincheckout.ui.absen

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chibatching.kotpref.Kotpref
import com.google.android.material.R.style.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.preferencesmodel.AbsenSettingsPreferences
import com.pklproject.checkincheckout.api.models.preferencesmodel.LoginPreferences
import com.pklproject.checkincheckout.databinding.FragmentAbsenBinding
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class AbsenFragment : Fragment(R.layout.fragment_absen) {

    private val binding: FragmentAbsenBinding by viewBinding()
    private val viewModel: ServiceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveServerClock()

        val absen = arguments?.getString(ABSEN_TYPE)

        when (absen) {
            "1" -> {
                binding.batasWaktuText.text = "Segera absen sebelum ${
                    AbsenSettingsPreferences.absenPagiAkhir
                }"
            }
            "2" -> {
                binding.batasWaktuText.text = "Segera absen sebelum ${
                    AbsenSettingsPreferences.absenSiangAkhir
                }"
            }
            "3" -> {
                binding.batasWaktuText.text = "Segera absen sebelum ${
                    AbsenSettingsPreferences.absenPulangAkhir
                }"
            }
        }

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
        setTextAppearance(requireContext())
        initialisation(absen.toString())
    }

    private fun initialisation(absen: String) {
        countDelayWaktuTersisa(absen)
        Kotpref.init(requireContext())
        if (viewModel.getResultPicture() != null && viewModel.getLatitude() != null || viewModel.getServerClock() != null) {
            binding.kirimabsen.isEnabled = true
            binding.txtSendError.isVisible = false

            binding.ambilfoto.setOnClickListener {
                findNavController().navigate(R.id.action_absenFragment_to_cameraView)
            }

            val username = LoginPreferences.username
            val password = LoginPreferences.password
            val keterangan = binding.keterangan.text

            binding.kirimabsen.setOnClickListener {
                val longitude = viewModel.getLongitude() ?: 0.0
                val latitude = viewModel.getLatitude() ?: 0.0
//                val jamSekarang = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                val jamSekarang = viewModel.getServerClock()
                kirimAbsen(
                    username,
                    password,
                    absen,
                    keterangan.toString(),
                    longitude,
                    latitude,
                    jamSekarang.toString()
                )
                binding.kirimabsen.isVisible = false
                binding.progressIndicator.isVisible = true
            }
            binding.progressIndicator.isVisible = false
        } else {
            binding.kirimabsen.isEnabled = false
            binding.ambilfoto.setOnClickListener {
                findNavController().navigate(R.id.action_absenFragment_to_cameraView)
            }
            if (viewModel.getServerClock() == null) {
                binding.txtSendError.text = "Jam server belum terambil, silahkan buka ulang aplikasi"
            } else {
                binding.txtSendError.text =
                    "Lokasi anda belum ditemukan, atau foto belum diambil coba tutup aplikasi dan nyalakan GPS anda lalu coba lagi"
            }
            binding.txtSendError.isVisible = true
            binding.progressIndicator.isVisible = false
        }
    }

    private fun countDelayWaktuTersisa(tipeAbsen: String) {
//        val waktuSekarang = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()).split(":")
        val waktuSekarang = viewModel.getServerClock()?.split(":")
        val jam = waktuSekarang?.get(0)?.toInt()
        val menit = waktuSekarang?.get(1)?.toInt()
        val detik = waktuSekarang?.get(2)?.toInt()

        val jamSekarang = Calendar.getInstance()
        jamSekarang.set(Calendar.HOUR_OF_DAY, jam!!)
        jamSekarang.set(Calendar.MINUTE, menit!!)
        jamSekarang.set(Calendar.SECOND, detik!!)

        when (tipeAbsen) {
            "1" -> {
                val waktuAkhirAbsenPagi = AbsenSettingsPreferences.absenPagiAkhir.split(":")
                val jamAbsenTerakhir = waktuAkhirAbsenPagi[0].toInt()
                val menitAbsenTerakhir = waktuAkhirAbsenPagi[1].toInt()
                val detikAbsenTerakhir = waktuAkhirAbsenPagi[2].toInt()

                val jamAbsenTerakhirCalendar = Calendar.getInstance()
                jamAbsenTerakhirCalendar.set(Calendar.HOUR_OF_DAY, jamAbsenTerakhir)
                jamAbsenTerakhirCalendar.set(Calendar.MINUTE, menitAbsenTerakhir)
                jamAbsenTerakhirCalendar.set(Calendar.SECOND, detikAbsenTerakhir)

                val delay = jamAbsenTerakhirCalendar.timeInMillis - jamSekarang.timeInMillis

                if (delay < 6000) {
                    binding.waktutTersisaTxt.text = "Anda terlambat"
                } else {
                    binding.waktutTersisaTxt.text = "Waktu tersisa: ${delay / 1000 / 60} menit"
                }
            }
            "2" -> {
                val waktuAkhirAbsenSiang = AbsenSettingsPreferences.absenSiangAkhir.split(":")
                val jamAbsenTerakhir = waktuAkhirAbsenSiang[0].toInt()
                val menitAbsenTerakhir = waktuAkhirAbsenSiang[1].toInt()
                val detikAbsenTerakhir = waktuAkhirAbsenSiang[2].toInt()

                val jamAbsenTerakhirCalendar = Calendar.getInstance()
                jamAbsenTerakhirCalendar.set(Calendar.HOUR_OF_DAY, jamAbsenTerakhir)
                jamAbsenTerakhirCalendar.set(Calendar.MINUTE, menitAbsenTerakhir)
                jamAbsenTerakhirCalendar.set(Calendar.SECOND, detikAbsenTerakhir)

                val delay = jamAbsenTerakhirCalendar.timeInMillis - jamSekarang.timeInMillis

                if (delay < 6000) {
                    binding.waktutTersisaTxt.text = "Anda terlambat"
                } else {
                    binding.waktutTersisaTxt.text = "Waktu tersisa: ${delay / 1000 / 60} menit"
                }
            }
            "3" -> {
                val waktuAkhirAbsenPulang = AbsenSettingsPreferences.absenPulangAkhir.split(":")
                val jamAbsenTerakhir = waktuAkhirAbsenPulang[0].toInt()
                val menitAbsenTerakhir = waktuAkhirAbsenPulang[1].toInt()
                val detikAbsenTerakhir = waktuAkhirAbsenPulang[2].toInt()

                val jamAbsenTerakhirCalendar = Calendar.getInstance()
                jamAbsenTerakhirCalendar.set(Calendar.HOUR_OF_DAY, jamAbsenTerakhir)
                jamAbsenTerakhirCalendar.set(Calendar.MINUTE, menitAbsenTerakhir)
                jamAbsenTerakhirCalendar.set(Calendar.SECOND, detikAbsenTerakhir)

                val delay = jamAbsenTerakhirCalendar.timeInMillis - jamSekarang.timeInMillis

                if (delay < 6000) {
                    binding.waktutTersisaTxt.text = "Anda terlambat"
                } else {
                    binding.waktutTersisaTxt.text = "Waktu tersisa: ${delay / 1000 / 60} menit"
                }
            }
        }
    }

    private fun kirimAbsen(
        username: String,
        password: String,
        tipeAbsen: String,
        keterangan: String,
        longitude: Double,
        latitude: Double,
        jamSekarang: String,
    ) {
        Toast.makeText(requireContext(), "Mohon untuk tidak menutup tab ini agar absen anda terkirim", Toast.LENGTH_LONG).show()
        val api = ApiInterface.createApi()
        val photo = viewModel.getBitmapImage()
//        val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val dateNow = viewModel.getServerDate()
        val namaUser = LoginPreferences.namaKaryawan
        val imageBodyPart = buildImageBodyPart("$namaUser-$tipeAbsen-$dateNow", photo!!)
        val reqBodyUsername = convertToRequstBody(username)
        val reqBodyPassword = convertToRequstBody(password)
        val reqBodyTipeAbsen = convertToRequstBody(tipeAbsen)
        val reqBodyKeterangan = convertToRequstBody(keterangan)
        val reqBodyLongitude = convertToRequstBody(longitude.toString())
        val reqBodyLatitude = convertToRequstBody(latitude.toString())
        val reqBodyJamSekarang = convertToRequstBody(jamSekarang)
        val reqBodyTanggalSekarang = convertToRequstBody(dateNow.toString())

        if (tipeAbsen == "1") {
            Log.d("longitude", longitude.toString())
            Log.d("latitude", latitude.toString())
            Log.d("username", username)
            Log.d("password", password)
            Log.d("tipeAbsen", tipeAbsen)
            Log.d("keterangan", keterangan)
            Log.d("jamSekarang", jamSekarang)
            Log.d("gambar", imageBodyPart.toString())
            lifecycleScope.launch {
                try {
                    val response = api.kirimAbsen(
                        reqBodyUsername,
                        reqBodyPassword,
                        reqBodyTipeAbsen,
                        reqBodyLongitude,
                        reqBodyLatitude,
                        imageBodyPart,
                        reqBodyKeterangan,
                        reqBodyJamSekarang,
                        reqBodyTanggalSekarang
                    )
                    retrieveTodayAbsen(ApiInterface.createApi(), username, password)
                    if (response.isSuccessful) {
                        Log.d("response", response.body().toString())
                        if (response.body()?.code == 200) {
                            binding.progressIndicator.isVisible = false
                            viewModel.setResultPicture(null)
                            viewModel.setBitmapImage(null)
                            findNavController().navigateUp()
                            Snackbar.make(
                                requireActivity().findViewById(R.id.container),
                                "Berhasil absen Pagi",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            Snackbar.make(
                                binding.root,
                                "Gagal menginput absen, silahkan coba lagi!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            Log.d("response", response.toString())
                            Log.d("longitude", longitude.toString())
                            Log.d("latitude", latitude.toString())
                            Log.d("username", username)
                            Log.d("password", password)
                            Log.d("tipeAbsen", tipeAbsen)
                            Log.d("keterangan", keterangan)
                            Log.d("jamSekarang", jamSekarang)
                            Log.d("gambar", imageBodyPart.toString())
                        }
                    } else {
                        binding.progressIndicator.isVisible = false
                        Snackbar.make(
                            binding.root,
                            "Gagal melakukan absen, silahkan coba lagi",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        Log.d("longitude", longitude.toString())
                        Log.d("latitude", latitude.toString())
                        Log.d("username", username)
                        Log.d("password", password)
                        Log.d("tipeAbsen", tipeAbsen)
                        Log.d("keterangan", keterangan)
                        Log.d("jamSekarang", jamSekarang)
                        Log.d("gambar", imageBodyPart.toString())
                    }
                } catch (e: Exception) {
                    binding.progressIndicator.isVisible = false
                    Log.d("Error", e.message.toString())
                    Snackbar.make(
                        binding.root,
                        "Gagal Absen, silahkan periksa internet anda lalu coba lagi!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            var namaAbsen = ""
            if (tipeAbsen == "2") {
                namaAbsen = "Siang"
            } else if (tipeAbsen == "3") {
                namaAbsen = "Pulang"
            }
            lifecycleScope.launch {
                try {
                    val response = api.kirimAbsen(
                        reqBodyUsername,
                        reqBodyPassword,
                        reqBodyTipeAbsen,
                        reqBodyLongitude,
                        reqBodyLatitude,
                        imageBodyPart,
                        reqBodyKeterangan,
                        reqBodyJamSekarang,
                        reqBodyTanggalSekarang
                    )
                    Log.d("response", response.body().toString())
                    retrieveTodayAbsen(ApiInterface.createApi(), username, password)
                    if (response.body()?.code == 200) {
                        binding.progressIndicator.isVisible = false
                        viewModel.setResultPicture(null)
                        viewModel.setBitmapImage(null)
                        findNavController().navigateUp()
                        Snackbar.make(
                            requireActivity().findViewById(R.id.container),
                            "Berhasil absen $namaAbsen",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        Log.d("response", response.toString())
                    } else {
                        binding.progressIndicator.isVisible = false
                        Snackbar.make(
                            binding.root,
                            "Gagal absen, silahkan coba lagi",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        Log.d("response", response.toString())
                    }
                } catch (e: Exception) {
                    binding.progressIndicator.isVisible = false
                    Log.d("Error", e.toString())
                    Snackbar.make(binding.root, "Gagal", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun buildImageBodyPart(fileName: String, bitmap: Bitmap): MultipartBody.Part {
        val leftImageFile = convertBitmapToFile(fileName, bitmap)
        val requestFile = leftImageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("photo_absen", "$fileName.jpg", requestFile)
    }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        val file = File(requireContext().cacheDir, fileName)
        file.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75 /*ignored for PNG*/, bos)
        val bitMapData = bos.toByteArray()
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    private fun convertToRequstBody(value: String): RequestBody {
        return value.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    private fun retrieveTodayAbsen(api:ApiInterface, username:String, password:String) {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        lifecycleScope.launch {
            try {
                val response = api.cekAbsenHariIni(username, password, todayDate)
                if (response.code == 200) {
                    viewModel.setTodayAttendance(response.absenHariIni)
                } else {
                    viewModel.setTodayAttendance(null)
                }
            } catch (e:Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun setTextAppearance(context: Context) {
        val appearanceSettings = Preferences(context).textSize
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.batasWaktuText.setTextAppearance(
                        requireContext(),
                        TextAppearance_AppCompat_Body1
                    )
                    binding.waktutTersisaTxt.setTextAppearance(
                        requireContext(),
                        TextAppearance_AppCompat_Body2
                    )
                    binding.previewTxt.setTextAppearance(
                        requireContext(),
                        TextAppearance_AppCompat_Body1
                    )
                }
                "normal" -> {
                    binding.batasWaktuText.setTextAppearance(
                        requireContext(),
                        TextAppearance_AppCompat_Large
                    )
                    binding.waktutTersisaTxt.setTextAppearance(
                        requireContext(),
                        TextAppearance_AppCompat_Medium
                    )
                    binding.previewTxt.setTextAppearance(
                        requireContext(),
                        TextAppearance_AppCompat_Large
                    )
                }
                "besar" -> {
                    binding.batasWaktuText.setTextAppearance(
                        requireContext(),
                        TextAppearance_AppCompat_Display1
                    )
                    binding.waktutTersisaTxt.setTextAppearance(
                        requireContext(),
                        TextAppearance_AppCompat_Large
                    )
                    binding.previewTxt.setTextAppearance(
                        requireContext(),
                        TextAppearance_AppCompat_Display1
                    )
                }
            }
        } else {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.batasWaktuText.setTextAppearance(TextAppearance_AppCompat_Body1)
                    binding.waktutTersisaTxt.setTextAppearance(TextAppearance_AppCompat_Body2)
                    binding.previewTxt.setTextAppearance(TextAppearance_AppCompat_Body1)
                }
                "normal" -> {
                    binding.batasWaktuText.setTextAppearance(TextAppearance_AppCompat_Large)
                    binding.waktutTersisaTxt.setTextAppearance(TextAppearance_AppCompat_Medium)
                    binding.previewTxt.setTextAppearance(TextAppearance_AppCompat_Large)
                }
                "besar" -> {
                    binding.batasWaktuText.setTextAppearance(TextAppearance_AppCompat_Display1)
                    binding.waktutTersisaTxt.setTextAppearance(TextAppearance_AppCompat_Large)
                    binding.previewTxt.setTextAppearance(TextAppearance_AppCompat_Display1)
                }
            }
        }
    }

    private fun retrieveServerClock() {
        lifecycleScope.launch {
            try {
                val response = ApiInterface.createApi().getDateNow()
                if (response.isSuccessful) {
                    viewModel.setServerClock(response.body()!!.clock)
                    viewModel.setServerDate(response.body()!!.date)
                } else {
                    viewModel.setServerClock(null)
                    viewModel.setServerDate(null)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Gagal mengambil jam server")
                        .setMessage("Anda mungkin tidak bisa absen sampai jam server tersedia")
                        .setPositiveButton("Ok") { _, _ ->

                        }
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    companion object {
        const val ABSEN_TYPE = "ABSENTYPEKEY"
    }
}