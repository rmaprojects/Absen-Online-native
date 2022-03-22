package com.pklproject.checkincheckout.ui.settings

import android.app.AppComponentFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.darkModeSwitcher.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Ubah tema aplikasi")
                .setSingleChoiceItems(arrayOf("System", "Light", "Dark"), AppCompatDelegate.getDefaultNightMode()) { _, which ->
                    when (which) {
                        0 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            Preferences(requireContext()).changeTheme = 0
                        }
                        1 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            Preferences(requireContext()).changeTheme = 1
                        }
                        2 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            Preferences(requireContext()).changeTheme = 2
                        }
                    }
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