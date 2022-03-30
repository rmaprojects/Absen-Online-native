package com.pklproject.checkincheckout.ui.profile

import android.content.Intent
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
            //TODO: Kasih alert dialog, kalau mau keluar ditanya dulu, ok atau tidak, jika ok maka keluar, jika tidak maka batal.
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin keluar?")
                .setPositiveButton("Ya"){_,_ ->
                    requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                    tinyDB.clear()
                    Preferences(requireContext()).isLoggedIn
                }
                .setNegativeButton("tidak"){_,_ ->
                    LoginActivity
                }
                .create()
                .show()
        }
    }

}