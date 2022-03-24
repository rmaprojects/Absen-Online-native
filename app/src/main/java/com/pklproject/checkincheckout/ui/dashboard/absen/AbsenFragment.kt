package com.pklproject.checkincheckout.ui.dashboard.absen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.FragmentAbsenBinding

class AbsenFragment : Fragment(R.layout.fragment_absen) {

    private val binding: FragmentAbsenBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var absen = arguments?.getString(ABSEN_TYPE)

        var tipeAbsen = ""

        if (absen == "pagi") {
            tipeAbsen = "1"
        } else if (absen == "siang") {
            tipeAbsen = "2"
        } else if (absen == "pulang") {
            tipeAbsen = "3"
        }

        //TODO:
        // bikin logic kirim absen, contohnya ada di AbsenMenuFragment. Tepatnya di function kirimAbsen(), Longitude dan latitude dummy aja.

    }

    companion object {
        const val ABSEN_TYPE = "ABSENTYPEKEY"
    }
}