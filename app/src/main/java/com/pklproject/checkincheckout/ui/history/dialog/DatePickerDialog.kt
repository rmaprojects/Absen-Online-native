package com.pklproject.checkincheckout.ui.history.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.MainActivity
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.BottomSheetDatePickerBinding
import com.pklproject.checkincheckout.ui.history.HistoryFragment
import com.pklproject.checkincheckout.ui.settings.TinyDB
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_date_picker, container, false)
    }

    private val binding: BottomSheetDatePickerBinding by viewBinding()
    private val viewModel:ServiceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yearItems = arrayListOf<String>()

        for (i in 2022..2032) {
            yearItems.add(i.toString())
        }

        binding.buttonYear.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Pilih Tahun")
                .setItems(yearItems.toTypedArray()) { _, item ->
                    val tahun = yearItems[item]
                    viewModel.setYear(tahun)
                    binding.buttonYear.text = tahun
                }
                .create()
                .show()
        }

        val months = arrayListOf(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
        )
        val monthNow = SimpleDateFormat("MM", Locale.getDefault()).format(System.currentTimeMillis())
        val yearNow = SimpleDateFormat("yyyy", Locale.getDefault()).format(System.currentTimeMillis())
        binding.buttonMonth.text = months[monthNow.toInt() - 1]
        binding.buttonYear.text = yearNow

        binding.buttonMonth.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Pilih Bulan")
                .setItems(months.toTypedArray()) { _, item ->
                    getMonthNametoNumber(months[item])
                    binding.buttonMonth.text = months[item]
                }
                .create()
                .show()
        }

        binding.btnShow.setOnClickListener {
            dismiss()
        }
    }

    private fun getMonthNametoNumber(itemMonth:String) {
        when(itemMonth) {
            "Januari" -> viewModel.setMonth("01")
            "Februari" -> viewModel.setMonth("02")
            "Maret" -> viewModel.setMonth("03")
            "April" -> viewModel.setMonth("04")
            "Mei" -> viewModel.setMonth("05")
            "Juni" -> viewModel.setMonth("06")
            "Juli" -> viewModel.setMonth("07")
            "Agustus" -> viewModel.setMonth("08")
            "September" -> viewModel.setMonth("09")
            "Oktober" -> viewModel.setMonth("10")
            "November" -> viewModel.setMonth("11")
            "Desember" -> viewModel.setMonth("12")
        }
    }
}