package com.pklproject.checkincheckout.ui.dashboard.absen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentMenuAbsenBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.TinyDB

class AbsenMenuFragment : Fragment(R.layout.fragment_menu_absen) {

    private val binding: FragmentMenuAbsenBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pilihanAbsen.check(R.id.izin)


        binding.pilihanAbsen.setOnCheckedChangeListener{ group, isChecked ->
            when(isChecked){
                R.id.absen ->{
                binding.absensi.isVisible = true
                binding.izindialog.isVisible = false
            }
            R.id.izin ->{
                binding.izindialog.isVisible = true
                binding.absensi.isVisible = false
            }
            R.id.cuti ->{
                binding.izindialog.isVisible = true
                binding.absensi.isVisible = false
            }
            }
        }
//        when(binding.pilihanAbsen.checkedRadioButtonId){
//            R.id.absen ->{
//                binding.absensi.isVisible = true
//                binding.izindialog.isVisible = false
//            }
//            R.id.izin ->{
//                binding.izindialog.isVisible = true
//                binding.absensi.isVisible = false
//            }
//            R.id.cuti ->{
//                binding.izindialog.isVisible = true
//                binding.absensi.isVisible = false
//            }
//        }

        //TODO:
        //  Tambahkan Logic ketika radio button memilih Izin atau Cuti, tampilkan tab izin atau cuti nya.
        //  Tampilkan tab absen ketika memilih absen
    }
}