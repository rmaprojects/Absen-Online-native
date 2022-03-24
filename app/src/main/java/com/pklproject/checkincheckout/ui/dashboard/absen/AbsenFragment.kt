package com.pklproject.checkincheckout.ui.dashboard.absen

import android.os.Bundle
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

class AbsenFragment : Fragment(R.layout.fragment_absen) {

    private val binding: FragmentAbsenBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tinyDB = TinyDB(requireContext())

        initialisation(tinyDB)
        //TODO:
        // bikin logic kirim absen, contohnya ada di AbsenMenuFragment. Tepatnya di function kirimAbsen(), Longitude dan latitude dummy aja.

        binding.ambilfoto.setOnClickListener {

        }
    }



    private fun initialisation(tinyDB: TinyDB) {
        var absen = arguments?.getString(ABSEN_TYPE)
        var tipeAbsen = ""

        if (absen == "pagi") {
            tipeAbsen = "1"
        } else if (absen == "siang") {
            tipeAbsen = "2"
        } else if (absen == "pulang") {
            tipeAbsen = "3"
        }

        val keterangan = binding.keterangan.text

        binding.kirimabsen.setOnClickListener {
            kirimAbsen(tipeAbsen,tinyDB,keterangan.toString())
        }
    }

    private fun kirimAbsen (tipeAbsen: String, tinyDB: TinyDB, keterangan:String) {


        val api = ApiInterface.createApi()
        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val longitude = -6.2848285
        val latitude = 107.1834223

        lifecycleScope.launch {
            val response = api.kirimAbsen(username.toString(), password.toString(), tipeAbsen, longitude, latitude, null, keterangan)

            try {
                if (response.isSuccessful){
                    Snackbar.make(binding.hasilfoto, "Data berhasil dikirim", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                } else {
                    Snackbar.make(binding.ambilfoto, "Gagal mengirim data, terjadi kesalahan", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                }
            } catch (e: Exception) {
                Snackbar.make(binding.ambilfoto, "Gagal mengambil data, aktifkan internet anda", Snackbar.LENGTH_SHORT)
                    .setAction("Ok") {}
                    .show()
            }

        }
    }



    companion object {
        const val ABSEN_TYPE = "ABSENTYPEKEY"
    }
}