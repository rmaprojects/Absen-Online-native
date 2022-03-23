package com.pklproject.checkincheckout.ui.dashboard.absen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.FragmentMenuAbsenBinding

class AbsenMenuFragment : Fragment(R.layout.fragment_menu_absen) {

    private val binding: FragmentMenuAbsenBinding by viewBinding(R.layout.fragment_menu_absen)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO:
        //  Tambahkan Logic ketika radio button memilih Izin atau Cuti, tampilkan tab izin atau cuti nya.
        //  Tampilkan tab absen ketika memilih absen
    }
}