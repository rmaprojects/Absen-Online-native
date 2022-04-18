package com.pklproject.checkincheckout.ui.settings

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.R.style.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
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
        setTextAppearance(requireContext())

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
        
        binding.switchAbsenSiangDiperlukan.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked || buttonView.isPressed) {
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
            } else if (!buttonView.isChecked) {
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
        }

        when (Preferences(requireContext()).changeTheme) {
            0 -> binding.currentThemeTxt.text = "Tema Sekarang: Sistem"
            1 -> binding.currentThemeTxt.text = "Tema Sekarang: Light"
            2 -> binding.currentThemeTxt.text = "Tema Sekarang: Dark"
        }

        binding.darkModeSwitcher.setOnClickListener {
            changeThemeDialog()
        }

        binding.ubahfontSlider.setLabelFormatter { value:Float ->
            when (value) {
                0F -> "Kecil"
                1F -> "Normal"
                2F -> "Besar"
                else -> "Kecil"
            }
        }

        binding.ubahfontSlider.addOnChangeListener { _, value, _ ->
            val ukuran: String
            when (value) {
                0F -> {
                    ukuran = "kecil"
                    Preferences(requireContext()).textSize = ukuran
                }
                1F -> {
                    ukuran = "normal"
                    Preferences(requireContext()).textSize = ukuran
                }
                2F -> {
                    ukuran = "besar"
                    Preferences(requireContext()).textSize = ukuran
                }
            }
        }

        binding.ubahfontSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                setTextAppearance(requireContext())
            }

            override fun onStopTrackingTouch(slider: Slider) {
                val appearanceSettings = Preferences(requireContext()).textSize
                when (slider.value) {
                    0F -> {
                        Preferences(requireContext()).textSize = "kecil"
                    }
                    1F -> {
                        Preferences(requireContext()).textSize = "normal"
                    }
                    2F -> {
                        Preferences(requireContext()).textSize = "besar"
                    }
                }
                when (appearanceSettings) {
                    "kecil" -> {
                        setTextAppearance(requireContext())
                    }
                    "normal" -> {
                        binding.ubahfontSlider.value = 1F
                        setTextAppearance(requireContext())
                    }
                    "besar" -> {
                        binding.ubahfontSlider.value = 2F
                        setTextAppearance(requireContext())
                    }
                }
            }
        })
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
            Snackbar.make(
                requireActivity().findViewById(R.id.container),
                "Gagal mengubah pengaturan absen siang",
                Snackbar.LENGTH_SHORT
            ).setAction("Ok") {
                binding.switchAbsenSiangDiperlukan.isChecked = !binding.switchAbsenSiangDiperlukan.isChecked
            }.show()
            Log.d("Error", e.message.toString())
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

    private fun setTextAppearance(context:Context) {
        val appearanceSettings = Preferences(context).textSize
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.ubahfontSlider.value = 0F
                }
                "normal" -> {
                    binding.ubahfontSlider.value = 1F
                }
                "besar" -> {
                    binding.ubahfontSlider.value = 2F
                }
            }
        } else {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.ubahfontSlider.value = 0F
                    binding.txtTitleModeGelap.setTextAppearance(TextAppearance_AppCompat_Body1)
                    binding.currentThemeTxt.setTextAppearance(TextAppearance_AppCompat_Body2)
                    binding.ukuranFonttxt.setTextAppearance(TextAppearance_AppCompat_Body1)
                }
                "normal" -> {
                    binding.ubahfontSlider.value = 1F
                    binding.txtTitleModeGelap.setTextAppearance(TextAppearance_AppCompat_Large)
                    binding.currentThemeTxt.setTextAppearance(TextAppearance_AppCompat_Medium)
                    binding.ukuranFonttxt.setTextAppearance(TextAppearance_AppCompat_Large)
                }
                "besar" -> {
                    binding.ubahfontSlider.value = 2F
                    binding.txtTitleModeGelap.setTextAppearance(TextAppearance_AppCompat_Display1)
                    binding.currentThemeTxt.setTextAppearance(TextAppearance_AppCompat_Large)
                    binding.ukuranFonttxt.setTextAppearance(TextAppearance_AppCompat_Display1)
                }
            }
        }
    }

    companion object{
        const val KEYKIRIMWAKTU = "KirimWaktu"
    }
}