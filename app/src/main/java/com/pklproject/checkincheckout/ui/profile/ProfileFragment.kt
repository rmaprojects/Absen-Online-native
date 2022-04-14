package com.pklproject.checkincheckout.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pklproject.checkincheckout.BuildConfig
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentProfileBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.ui.settings.SettingsFragment
import com.pklproject.checkincheckout.ui.settings.TinyDB

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tinyDB = TinyDB(requireContext())

        binding.name.text = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).namaKaryawan
        binding.jabatan.text = tinyDB.getObject(LoginActivity.KEYSIGNIN,LoginModel::class.java).jabatan
        binding.departemen.text = tinyDB.getObject(LoginActivity.KEYSIGNIN,LoginModel::class.java).departement
        binding.unit.text = tinyDB.getObject(LoginActivity.KEYSIGNIN,LoginModel::class.java).businessUnit
        binding.nomorVersi.text = "Version: ${BuildConfig.VERSION_NAME}"

        binding.keluar.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin keluar?")
                .setPositiveButton("Ya"){ _, _ ->
                    tinyDB.clear()
                    Preferences(requireContext()).isLoggedIn = false
                    requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Tidak"){ _, _ ->
                }
                .create()
                .show()
        }
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
                    binding.name.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.jabatan.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body2)
                    binding.departemen.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.unit.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                }
                "normal" -> {
//                    binding.ubahfontSlider.value = 1F
                    binding.name.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
                    binding.jabatan.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Medium)
                    binding.departemen.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.unit.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                }
                "besar" -> {
//                    binding.ubahfontSlider.value = 2F
                    binding.name.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Large)
                    binding.jabatan.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Medium)
                    binding.departemen.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                    binding.unit.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body1)
                }
            }
        }
    }

}