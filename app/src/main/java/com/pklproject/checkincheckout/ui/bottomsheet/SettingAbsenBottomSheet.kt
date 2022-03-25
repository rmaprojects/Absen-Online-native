package com.pklproject.checkincheckout.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.BottomsheetJadwalAbsenBinding
import com.pklproject.checkincheckout.databinding.FragmentSettingsBinding
import com.pklproject.checkincheckout.ui.settings.SettingsFragment.Companion.KEYKIRIMWAKTU
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel

class SettingAbsenBottomSheet : BottomSheetDialogFragment(){

    private val binding :BottomsheetJadwalAbsenBinding by viewBinding()
    private val viewModel :ServiceViewModel by activityViewModels()

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

//        var isAbsenSiangEnabled:Boolean

        val listAbsenSettings = viewModel.getAbsenSettings().value

//        isAbsenSiangEnabled = listAbsenSettings?.get(6) == "1"

        binding.pagi.text = "Menampilkan waktu absen untuk: $waktu"

//        when (waktu) {
//            "Pagi" -> {
//                binding.jammulai.text = listAbsenSettings?.get(0)
//                binding.jamsampai.text = listAbsenSettings?.get(1)
//            }
//            "Siang" -> {
//                binding.jammulai.text = listAbsenSettings?.get(2)
//                binding.jamsampai.text = listAbsenSettings?.get(3)
//            }
//            "Pulang" -> {
//                binding.jammulai.text = listAbsenSettings?.get(4)
//                binding.jamsampai.text = listAbsenSettings?.get(5)
//            }
//        }
    }

}