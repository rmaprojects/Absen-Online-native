package com.pklproject.checkincheckout.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.BottomsheetJadwalAbsenBinding
import com.pklproject.checkincheckout.databinding.FragmentSettingsBinding
import com.pklproject.checkincheckout.ui.settings.SettingsFragment.Companion.KEYKIRIMWAKTU

class SettingAbsenBottomSheet : BottomSheetDialogFragment(){

    private val binding :BottomsheetJadwalAbsenBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_jadwal_absen, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val waktu = arguments?.getString(KEYKIRIMWAKTU) ?:""

        binding.pagi.text = "Menampilkan Waktu untuk: $waktu"

    }

}