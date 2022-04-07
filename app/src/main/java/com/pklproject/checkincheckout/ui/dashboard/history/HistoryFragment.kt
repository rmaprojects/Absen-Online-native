package com.pklproject.checkincheckout.ui.dashboard.history

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentHistoryBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.dashboard.history.item.HistoryItem
import com.pklproject.checkincheckout.ui.settings.TinyDB
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val binding: FragmentHistoryBinding by viewBinding()
    private val viewModel:ServiceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tinyDb = TinyDB(requireContext())

        retrieveHistory(tinyDb)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.selectMonthButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_history_to_datePickerDialog)
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
                    viewModel.setHistory(response.body()!!)
                    binding.recyclerView.adapter = HistoryItem(response.body()!!)
                } else {
                    Log.d("History", response.body()?.history.toString())
                }
            } catch (e:Exception) {
                Log.d("Error", e.toString())
                Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengambil data riwayat", Snackbar.LENGTH_LONG)
                    .setAction("Ok") {}
                    .show()
            }
        }
    }

}