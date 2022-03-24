package com.pklproject.checkincheckout.ui.dashboard.absen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentMenuAbsenBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.ui.settings.TinyDB
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AbsenMenuFragment : Fragment(R.layout.fragment_menu_absen) {

    private val binding: FragmentMenuAbsenBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialisation()

        var tipeAbsen = ""

        binding.pilihanAbsen.setOnCheckedChangeListener{ _, isChecked ->
            when(isChecked){
                R.id.absen -> {
                    binding.absensi.isVisible = true
                    binding.izindialog.isVisible = false
                }
                R.id.izin -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    tipeAbsen = "izin"
                }
                R.id.cuti -> {
                    binding.izindialog.isVisible = true
                    binding.absensi.isVisible = false
                    tipeAbsen = "cuti"
                }
            }
        }

        binding.absenpagi.setOnClickListener {
            goToAbsensi("pagi")
        }

        binding.absensiang.setOnClickListener {
            goToAbsensi("siang")
        }

        binding.absenpulang.setOnClickListener {
            goToAbsensi("pulang")
        }

    }

    private fun initialisation() {
        binding.pilihanAbsen.check(R.id.absen)
        binding.absensi.isVisible = true
        binding.izindialog.isVisible = false

        val tinyDB = TinyDB(requireContext())
        val api = ApiInterface.createApi()
        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val hariIni = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        lifecycleScope.launch {
            val response = api.cekAbsenHariIni(username.toString(), password.toString(), hariIni)
            try {
                if (response.isSuccessful) {
                    val statusAbsen = response.body()!!.absenHariIni
                    if (statusAbsen!!.isEmpty()) {
                        binding.absenpagi.isClickable= true
                        binding.absensiang.isClickable = false
                        binding.absenpulang.isClickable = false
                    }
                    when (statusAbsen[0].tipeAbsen) {
                        "pagi" -> {
                            binding.absenpagi.isClickable = false
                            binding.absensiang.isClickable = true
                            binding.absenpulang.isClickable = false
                        }
                        "siang" -> {
                            binding.absenpagi.isClickable = false
                            binding.absensiang.isClickable = false
                            binding.absenpulang.isClickable = true
                        }
                        "pulang" -> {
                            binding.absenpagi.isClickable = false
                            binding.absensiang.isClickable = false
                            binding.absenpulang.isClickable = false
                        }
                    }
                } else {
                    Snackbar.make(
                        requireView(),
                        "Gagal mengambil data absen",
                        Snackbar.LENGTH_SHORT).setAction("Ok") {}
                        .show()
                }
            } catch (e: Exception) {
                Snackbar.make(binding.rootLayout, "Gagal mengambil data, aktifkan internet anda", Snackbar.LENGTH_SHORT)
                    .setAction("Ok") {}
                    .show()
            }
        }
    }

    private fun goToAbsensi(tipeAbsen:String) {
        val bundle = bundleOf(AbsenFragment.ABSEN_TYPE to tipeAbsen)
        findNavController().navigate(R.id.action_navigation_absen_to_absenFragment, bundle)
    }
}