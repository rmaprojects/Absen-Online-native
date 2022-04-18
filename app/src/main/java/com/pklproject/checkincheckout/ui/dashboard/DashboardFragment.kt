package com.pklproject.checkincheckout.ui.dashboard

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.R.color.design_default_color_error
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.FragmentDashboardBinding
import com.pklproject.checkincheckout.ui.settings.Preferences
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.R.style.*

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val binding: FragmentDashboardBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentClock = SimpleDateFormat("hh:mm", Locale.getDefault()).format(Date())
        setTextAppearance(requireContext())
        binding.txtCurrentClock.text = currentClock
    }


    private fun setTextAppearance(context: Context) {
        val appearanceSettings = Preferences(context).textSize
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.jamSekarang.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.txtCurrentClock.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.filter.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.kehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleSmall)
                    binding.persenkehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.ketidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleSmall)
                    binding.persenKetidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.terlambat.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.terlambat.setTextColor(resources.getColor(design_default_color_error))
                }
                "normal" -> {
                    binding.jamSekarang.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.txtCurrentClock.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.filter.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.kehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.persenkehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.ketidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.persenKetidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                }
                "besar" -> {
                    binding.jamSekarang.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineLarge)
                    binding.txtCurrentClock.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineLarge)
                    binding.filter.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineLarge)
                    binding.kehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineSmall)
                    binding.persenkehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineLarge)
                    binding.ketidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineSmall)
                    binding.persenKetidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineSmall)
                }
            }
        } else {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.jamSekarang.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.txtCurrentClock.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.filter.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.kehadiran.setTextAppearance(TextAppearance_Material3_TitleSmall)
                    binding.persenkehadiran.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.ketidakhadiran.setTextAppearance(TextAppearance_Material3_TitleSmall)
                    binding.persenKetidakhadiran.setTextAppearance(TextAppearance_Material3_TitleMedium)
                }
                "normal" -> {
                    binding.jamSekarang.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.txtCurrentClock.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.filter.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.kehadiran.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.persenkehadiran.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.ketidakhadiran.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.persenKetidakhadiran.setTextAppearance(TextAppearance_Material3_TitleLarge)
                }
                "besar" -> {
                    binding.jamSekarang.setTextAppearance(TextAppearance_Material3_HeadlineLarge)
                    binding.txtCurrentClock.setTextAppearance(TextAppearance_Material3_HeadlineLarge)
                    binding.filter.setTextAppearance(TextAppearance_Material3_HeadlineLarge)
                    binding.kehadiran.setTextAppearance(TextAppearance_Material3_HeadlineSmall)
                    binding.persenkehadiran.setTextAppearance(TextAppearance_Material3_HeadlineLarge)
                    binding.ketidakhadiran.setTextAppearance(TextAppearance_Material3_HeadlineSmall)
                    binding.persenKetidakhadiran.setTextAppearance(TextAppearance_Material3_HeadlineLarge)
                }
            }
        }
    }
}