package com.pklproject.checkincheckout.ui.settings

import android.app.AppComponentFactory
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentSettingsBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: 
        // Arahkan tombol atur jadwal absen ke bottom sheet, jika diklik maka bottom sheet akan muncul
        // Kasih ID dulu

        val tinyDB = TinyDB(requireContext())
        val isAdmin = tinyDB.getObject(LoginActivity.KEYSIGNIN,LoginModel::class.java).statusAdmin

        binding.adminOnlyArea.isVisible = isAdmin == "1"

        when (Preferences(requireContext()).changeTheme){
            0 -> binding.currentThemeTxt.text = "Tema Sekarang: Sistem"
            1 -> binding.currentThemeTxt.text = "Tema Sekarang: Light"
            2 -> binding.currentThemeTxt.text = "Tema Sekarang: Dark"
        }

        binding.darkModeSwitcher.setOnClickListener {
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
                    Preferences(requireContext()).changeTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                .create()
                .show()
        }


    }
}