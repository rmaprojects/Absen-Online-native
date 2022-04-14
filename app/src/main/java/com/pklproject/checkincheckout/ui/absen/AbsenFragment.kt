package com.pklproject.checkincheckout.ui.absen

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
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
import com.pklproject.checkincheckout.MainActivity
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.api.models.Setting
import com.pklproject.checkincheckout.databinding.FragmentAbsenBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.ui.settings.TinyDB
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
import com.google.android.material.R.style.*
import kotlin.time.Duration.Companion.minutes

class AbsenFragment : Fragment(R.layout.fragment_absen) {

    private val binding: FragmentAbsenBinding by viewBinding()
    private val viewModel: ServiceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val absen = arguments?.getString(ABSEN_TYPE)
        val tinyDB = TinyDB(requireContext())

        when (absen) {
            "1" -> {
                binding.batasWaktuText.text = "Segera absen sebelum ${
                    tinyDB.getObject(
                        MainActivity.PENGATURANABSENKEY,
                        Setting::class.java
                    ).absenPagiAkhir
                }"
            }
            "2" -> {
                binding.batasWaktuText.text = "Segera absen sebelum ${
                    tinyDB.getObject(
                        MainActivity.PENGATURANABSENKEY,
                        Setting::class.java
                    ).absenSiangAkhir
                }"
            }
            "3" -> {
                binding.batasWaktuText.text = "Segera absen sebelum ${
                    tinyDB.getObject(
                        MainActivity.PENGATURANABSENKEY,
                        Setting::class.java
                    ).absenPulangAkhir
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
        initialisation(tinyDB, absen.toString())
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

    private fun initialisation(tinyDB: TinyDB, absen: String) {
        countDelayWaktuTersisa(absen)
        if (viewModel.getResultPicture() != null && viewModel.getLatitude() != null) {
            binding.kirimabsen.isEnabled = true

            binding.ambilfoto.setOnClickListener {
                findNavController().navigate(R.id.action_absenFragment_to_cameraView)
            }

            val username =
                tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
            val password =
                tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
            val keterangan = binding.keterangan.text

            binding.kirimabsen.setOnClickListener {
                val longitude = viewModel.getLongitude() ?: 0.0
                val latitude = viewModel.getLatitude() ?: 0.0
                val jamSekarang = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                kirimAbsen(
                    username!!,
                    password!!,
                    absen,
                    keterangan.toString(),
                    longitude,
                    latitude,
                    jamSekarang
                )
            }
        } else {
            binding.kirimabsen.isEnabled = false
            binding.ambilfoto.setOnClickListener {
                findNavController().navigate(R.id.action_absenFragment_to_cameraView)
            }
        }
    }

    private fun countDelayWaktuTersisa(tipeAbsen: String) {
        val waktuSekarang = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()).split(":")
        val jam = waktuSekarang[0].toInt()
        val menit = waktuSekarang[1].toInt()
        val detik = waktuSekarang[2].toInt()

        val jamSekarang = Calendar.getInstance()
        jamSekarang.set(Calendar.HOUR_OF_DAY, jam)
        jamSekarang.set(Calendar.MINUTE, menit)
        jamSekarang.set(Calendar.SECOND, detik)

        when (tipeAbsen) {
            "1" -> {
                val waktuAkhirAbsenPagi = TinyDB(requireContext()).getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java).absenPagiAkhir.split(":")
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
                val waktuAkhirAbsenSiang = TinyDB(requireContext()).getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java).absenSiangAkhir.split(":")
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
                val waktuAkhirAbsenPulang = TinyDB(requireContext()).getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java).absenPulangAkhir.split(":")
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
        val tinyDB = TinyDB(requireContext())
        val api = ApiInterface.createApi()
        val photo = viewModel.getBitmapImage()
        val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val imageBodyPart = buildImageBodyPart("$username-$tipeAbsen-$dateNow", photo!!)
        val reqBodyUsername = convertToRequstBody(username)
        val reqBodyPassword = convertToRequstBody(password)
        val reqBodyTipeAbsen = convertToRequstBody(tipeAbsen)
        val reqBodyKeterangan = convertToRequstBody(keterangan)
        val reqBodyLongitude = convertToRequstBody(longitude.toString())
        val reqBodyLatitude = convertToRequstBody(latitude.toString())
        val reqBodyJamSekarang = convertToRequstBody(jamSekarang)
        val idAbsensiReqBody = convertToRequstBody(tinyDB.getString(KEYIDABSEN))
        val reqBodyTanggalSekarang = convertToRequstBody(dateNow)

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
                        idAbsensiReqBody,
                        reqBodyTanggalSekarang
                    )
                    if (response.isSuccessful) {
                        Log.d("response", response.body().toString())
                        if (response.body()?.code == 200) {
                            tinyDB.putString(KEYIDABSEN, response.body()?.idAbsensi)
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
                                "Gagal absen, silahkan coba lagi",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            Log.d("response", response.toString())
                        }
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Gagal melakukan absen, silahkan coba lagi",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
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
            Log.d("ID", tinyDB.getString(KEYIDABSEN))
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
                        idAbsensiReqBody,
                        reqBodyTanggalSekarang
                    )
                    Log.d("response", response.body().toString())
                    if (response.body()?.code == 200) {
                        viewModel.setResultPicture(null)
                        findNavController().navigateUp()
                        Snackbar.make(
                            requireActivity().findViewById(R.id.container),
                            "Berhasil absen $namaAbsen",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        Log.d("response", response.toString())
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Gagal absen, silahkan coba lagi",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        Log.d("response", response.toString())
                    }
                } catch (e: Exception) {
                    Log.d("Error", e.toString())
                    Snackbar.make(binding.root, "Gagal", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun buildImageBodyPart(fileName: String, bitmap: Bitmap): MultipartBody.Part {
        val leftImageFile = convertBitmapToFile(fileName, bitmap)
//        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), leftImageFile)
        val requestFile = leftImageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("photo_absen", "$fileName.jpg", requestFile)
    }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        val file = File(requireContext().cacheDir, fileName)
        file.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
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

    companion object {
        const val ABSEN_TYPE = "ABSENTYPEKEY"
        const val KEYIDABSEN = "IDABSENKEY"
    }
}