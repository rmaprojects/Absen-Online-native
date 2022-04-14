package com.pklproject.checkincheckout.ui.history

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentHistoryBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.history.item.HistoryItem
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.ui.settings.TinyDB
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import com.google.android.material.R.style.*
import kotlinx.coroutines.launch

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val binding: FragmentHistoryBinding by viewBinding()
    private val viewModel:ServiceViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
        retrieveHistory(TinyDB(requireContext()))
        setTextAppearance(requireContext())

        binding.selectMonthButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_history_to_datePickerDialog)
        }

        viewModel.listener = {
            retrieveHistory(TinyDB(requireContext()))
            binding.selectMonthButton.text = "${getProperMonth(viewModel.getMonth())} ${viewModel.getYear()}"
        }

        binding.selectMonthButton.text = "${getProperMonth(viewModel.getMonth())} ${viewModel.getYear()}"

        binding.recyclerView.setAdapter(HistoryItem(viewModel.getHistoryData()))
    }

    private fun setTextAppearance(context: Context) {
        val appearanceSettings = Preferences(context).textSize
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.filter1.setTextAppearance(requireContext(),TextAppearance_AppCompat_Body1)
                    binding.selectMonthButton.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                }
                "normal" -> {
                    binding.filter1.setTextAppearance(requireContext(), TextAppearance_AppCompat_Large)
                    binding.selectMonthButton.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineSmall)

                }
                "besar" -> {
                    binding.filter1.setTextAppearance(requireContext(),TextAppearance_AppCompat_Display1)
                    binding.selectMonthButton.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineLarge)
                }
            }
        } else {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.filter1.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.selectMonthButton.setTextAppearance(TextAppearance_Material3_TitleMedium)
                }
                "normal" -> {
                    binding.filter1.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.selectMonthButton.setTextAppearance(TextAppearance_Material3_HeadlineSmall)
                }
                "besar" -> {
                    binding.filter1.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.selectMonthButton.setTextAppearance(TextAppearance_Material3_HeadlineLarge)
                }
            }
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
                    binding.recyclerView.setAdapter(HistoryItem(viewModel.getHistoryData()))
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

    private fun getProperMonth(month:String?) : String {
        return when(month?.toInt()) {
            1 -> "Januari"
            2 -> "Februari"
            3 -> "Maret"
            4 -> "April"
            5 -> "Mei"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "Agustus"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> "Unknown Month"
        }
    }

}