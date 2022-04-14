package com.pklproject.checkincheckout.ui.dashboard

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.FragmentDashboardBinding
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.ui.settings.SettingsFragment
import com.pklproject.checkincheckout.ui.settings.TinyDB
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val binding: FragmentDashboardBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentClock = SimpleDateFormat("hh:mm", Locale.getDefault()).format(Date())

        binding.txtCurrentClock.text = currentClock

        setTextAppearance(requireContext())

    }
    private fun setTextAppearance(context: Context) {
        val appearanceSettings = Preferences(context).textSize
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            when (appearanceSettings) {
//                "kecil" -> {
//                    binding.ubahfontSlider.value = 0F
//                }
//                "normal" -> {
//                    binding.ubahfontSlider.value = 1F
//                }
//                "besar" -> {
//                    binding.ubahfontSlider.value = 2F
//                }
            }
        } else {
            when (appearanceSettings) {
                "kecil" -> {
//                    binding.ubahfontSlider.value = 0F
                    binding.jamSekarang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.txtCurrentClock.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body2)
                    binding.filter.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.Pekan.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.kehadiran.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.persenkehadiran.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.kehadiran1.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.persenkehadiran1.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.terlambat.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                }
                "normal" -> {
//                    binding.ubahfontSlider.value = 1F
                    binding.jamSekarang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.txtCurrentClock.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body2)
                    binding.filter.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.Pekan.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.kehadiran.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.persenkehadiran.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.kehadiran1.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.persenkehadiran1.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.terlambat.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                }
                "besar" -> {
//                    binding.ubahfontSlider.value = 2F
                    binding.jamSekarang.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.txtCurrentClock.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body2)
                    binding.filter.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.Pekan.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.kehadiran.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.persenkehadiran.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.kehadiran1.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.persenkehadiran1.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.terlambat.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                }
            }
        }
    }
}