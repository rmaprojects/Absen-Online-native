package com.pklproject.checkincheckout.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.R.style.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pklproject.checkincheckout.BuildConfig
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.models.preferencesmodel.LoginPreferences
import com.pklproject.checkincheckout.databinding.FragmentProfileBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.Preferences

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTextAppearance(requireContext())

        binding.name.text = LoginPreferences.namaKaryawan
        binding.jabatan.text = LoginPreferences.jabatan
        binding.departemen.text = LoginPreferences.departement
        binding.unit.text = LoginPreferences.businessUnit
        binding.nomorVersi.text = "Version: ${BuildConfig.VERSION_NAME}"

        binding.keluar.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin keluar?")
                .setPositiveButton("Ya"){ _, _ ->
                    LoginPreferences.clear()
                    Preferences(requireContext()).isLoggedIn = false
                    requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Tidak"){ _, _ ->
                }
                .create()
                .show()
        }
    }

    private fun setTextAppearance(context: Context) {
        val appearanceSettings = Preferences(context).textSize
        val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.name.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.name.typeface = boldTypeface
                    binding.jabatan.setTextAppearance(requireContext(), TextAppearance_Material3_TitleSmall)
                    binding.jabatan.typeface = boldTypeface
                    binding.departemen.setTextAppearance(requireContext(), TextAppearance_Material3_TitleSmall)
                    binding.departemen.typeface = boldTypeface
                    binding.unit.setTextAppearance(requireContext(), TextAppearance_Material3_TitleSmall)
                    binding.unit.typeface = boldTypeface
                    binding.txtJabatan.setTextAppearance(requireContext(), TextAppearance_Material3_TitleSmall)
                    binding.txtDepartemen.setTextAppearance(requireContext(), TextAppearance_Material3_TitleSmall)
                    binding.txtBisnisUnit.setTextAppearance(requireContext(), TextAppearance_Material3_TitleSmall)
                }
                "normal" -> {
                    binding.name.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.name.typeface = boldTypeface
                    binding.jabatan.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.jabatan.typeface = boldTypeface
                    binding.departemen.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.departemen.typeface = boldTypeface
                    binding.unit.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.unit.typeface = boldTypeface
                    binding.txtJabatan.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.txtDepartemen.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.txtBisnisUnit.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                }
                "besar" -> {
                    binding.name.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineSmall)
                    binding.name.typeface = boldTypeface
                    binding.jabatan.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.jabatan.typeface = boldTypeface
                    binding.departemen.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.departemen.typeface = boldTypeface
                    binding.unit.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.unit.typeface = boldTypeface
                    binding.txtJabatan.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.txtDepartemen.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.txtBisnisUnit.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                }
            }
        } else {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.name.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.name.typeface = boldTypeface
                    binding.jabatan.setTextAppearance(TextAppearance_Material3_TitleSmall)
                    binding.jabatan.typeface = boldTypeface
                    binding.departemen.setTextAppearance(TextAppearance_Material3_TitleSmall)
                    binding.departemen.typeface = boldTypeface
                    binding.unit.setTextAppearance(TextAppearance_Material3_TitleSmall)
                    binding.unit.typeface = boldTypeface
                    binding.txtJabatan.setTextAppearance(TextAppearance_Material3_TitleSmall)
                    binding.txtDepartemen.setTextAppearance(TextAppearance_Material3_TitleSmall)
                    binding.txtBisnisUnit.setTextAppearance(TextAppearance_Material3_TitleSmall)
                }
                "normal" -> {
                    binding.name.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.name.typeface = boldTypeface
                    binding.jabatan.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.jabatan.typeface = boldTypeface
                    binding.departemen.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.departemen.typeface = boldTypeface
                    binding.unit.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.unit.typeface = boldTypeface
                    binding.txtJabatan.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.txtDepartemen.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.txtBisnisUnit.setTextAppearance(TextAppearance_Material3_TitleMedium)
                }
                "besar" -> {
                    binding.name.setTextAppearance(TextAppearance_Material3_HeadlineSmall)
                    binding.name.typeface = boldTypeface
                    binding.jabatan.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.jabatan.typeface = boldTypeface
                    binding.departemen.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.departemen.typeface = boldTypeface
                    binding.unit.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.unit.typeface = boldTypeface
                    binding.txtJabatan.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.txtDepartemen.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.txtBisnisUnit.setTextAppearance(TextAppearance_Material3_TitleLarge)
                }
            }
        }
    }

}