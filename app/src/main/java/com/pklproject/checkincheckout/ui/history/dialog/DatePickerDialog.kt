package com.pklproject.checkincheckout.ui.history.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.BottomSheetDatePickerBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.history.HistoryFragment
import com.pklproject.checkincheckout.ui.settings.TinyDB
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
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

        binding.buttonMonth.text = months[viewModel.getMonth()?.toInt()?.minus(1) ?: Calendar.getInstance()[Calendar.MONTH] -1]
        binding.buttonYear.text = viewModel.getYear()

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
            viewModel.listener?.invoke()
            dismiss()
        }
    }

    private fun retrieveHistory(tinyDB: TinyDB) {
        val username = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).username
        val password = tinyDB.getObject(LoginActivity.KEYSIGNIN, LoginModel::class.java).password
        val currentYear = viewModel.getYear()
        val currentMonth = viewModel.getMonth()
        val api = ApiInterface.createApi()
        lifecycleScope.launch {
            try {
                val response = api.history(username.toString(), password.toString(), currentYear.toString(), currentMonth.toString())
                if (response.isSuccessful) {
                    Log.d("History", response.body()?.history.toString())
                    viewModel.setHistoryData(response.body()!!)
                    true
                } else {
                    Log.d("History", response.body()?.history.toString())
                    false
                }
            } catch (e:Exception) {
                Log.d("Error", e.toString())
                Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengambil data riwayat", Snackbar.LENGTH_LONG)
                    .setAction("Ok") {}
                    .show()
            }
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