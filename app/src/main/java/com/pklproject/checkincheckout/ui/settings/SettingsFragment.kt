package com.pklproject.checkincheckout.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
import com.pklproject.checkincheckout.databinding.FragmentSettingsBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tinyDB = TinyDB(requireContext())
        val isAdmin = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).statusAdmin

        initialisation(tinyDB)

        binding.adminOnlyArea.isVisible = isAdmin == "1"
    }

    private fun initialisation(tinyDB: TinyDB) {

        val listAbsenSettings = tinyDB.getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java)

        binding.pagi.setOnClickListener {
            val bundle = bundleOf(KEYKIRIMWAKTU to "Pagi")
            findNavController().navigate(R.id.action_settingsFragment_to_settingAbsenBottomSheet, bundle)
        }
        binding.siang.setOnClickListener {
            val bundle = bundleOf(KEYKIRIMWAKTU to "Siang")
            findNavController().navigate(R.id.action_settingsFragment_to_settingAbsenBottomSheet, bundle)
        }
        binding.pulang.setOnClickListener {
            val bundle = bundleOf(KEYKIRIMWAKTU to "Pulang")
            findNavController().navigate(R.id.action_settingsFragment_to_settingAbsenBottomSheet, bundle)
        }

        val isAbsenSiangEnabled:Boolean = listAbsenSettings.absenSiangDiperlukan == "1"

        binding.switchAbsenSiangDiperlukan.isChecked = isAbsenSiangEnabled

        binding.switchAbsenSiangDiperlukan.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin mengubah pengaturan absen siang?")
                .setPositiveButton("Ya") { _, _ ->
                    changeIfAbsenSiangDiperlukan(tinyDB)
                }
                .setNegativeButton("Tidak") { _, _ ->
                    binding.switchAbsenSiangDiperlukan.isChecked = isAbsenSiangEnabled
                }
                .create()
                .show()
        }

        when (Preferences(requireContext()).changeTheme) {
            0 -> binding.currentThemeTxt.text = "Tema Sekarang: Sistem"
            1 -> binding.currentThemeTxt.text = "Tema Sekarang: Light"
            2 -> binding.currentThemeTxt.text = "Tema Sekarang: Dark"
        }

        binding.darkModeSwitcher.setOnClickListener {
            changeThemeDialog()
        }
    }

    private fun changeIfAbsenSiangDiperlukan(tinyDB: TinyDB) {
        val api = ApiInterface.createApi()
        try {
            lifecycleScope.launch {
                val response = api.updateSettingsAbsen("4", null, null)
                if (response.status) {
                    Snackbar.make(
                        requireView(),
                        "Berhasil mengubah pengaturan absen siang",
                        Snackbar.LENGTH_SHORT
                    ).setAction("Ok"){}.show()
                    MainActivity().retrieveSettingsAbsen(tinyDB)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Gagal mengubah pengaturan absen siang",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeThemeDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Ubah tema aplikasi")
            .setItems(arrayOf("Sistem", "Light", "Dark")) { _, i ->
                when (i) {
                    0 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        binding.currentThemeTxt.text = "Tema Sekarang: Sistem"
                    }
                    1 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        binding.currentThemeTxt.text = "Tema Sekarang: Light"
                    }
                    2 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        binding.currentThemeTxt.text = "Tema Sekarang: Dark"
                    }
                }
                Preferences(requireContext()).changeTheme = i
            }
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Reset") { dialog, _ ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                dialog.dismiss()
                Preferences(requireContext()).changeTheme =
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            .create()
            .show()
    }

    companion object{
        const val KEYKIRIMWAKTU = "KirimWaktu"
    }
}