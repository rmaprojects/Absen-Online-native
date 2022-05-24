package com.pklproject.checkincheckout.ui.dashboard

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chibatching.kotpref.Kotpref
import com.google.android.material.R.style.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.preferencesmodel.LoginPreferences
import com.pklproject.checkincheckout.databinding.FragmentDashboardBinding
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
import org.joda.time.DateTimeConstants
import org.joda.time.LocalDate
import java.text.SimpleDateFormat
import java.util.*
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.models.apimodel.PercentageModel

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val binding: FragmentDashboardBinding by viewBinding()
    private val viewModel: ServiceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onStart() {
        super.onStart()
        applyFilterPekan()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Kotpref.init(requireContext())
        setTextAppearance(requireContext())

        retrieveServerClock()

        val mainLooperHandler = Handler(Looper.getMainLooper())

        mainLooperHandler.postDelayed( {
            retrieveServerClock()
        }, 60000)

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTheme(ThemeOverlay_Material3_MaterialCalendar)
            .setTitleText("Pilih range tanggal")
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            applyFilterCustom(selection.first!!, selection.second!!)
        }

        binding.btnFilter.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Ubah Filter")
                .setItems(arrayOf("Pekan ini", "Bulan ini", "Atur sendiri")) { dialog, which ->
                    when (which) {
                        0 -> {
                            applyFilterPekan()
                        }
                        1 -> {
                            applyFilterBulan()
                        }
                        2 -> {
                            dateRangePicker.show(requireActivity().supportFragmentManager, "DATE_RANGE_PICKER")
                            dateRangePicker.addOnNegativeButtonClickListener { dialog.dismiss() }
                        }
                    }
                }
                .setNegativeButton("Batal") { _,_ -> }
                .create()
                .show()
        }
    }

    private fun applyFilterBulan() {
        val username = LoginPreferences.username
        val password = LoginPreferences.password
        val now = LocalDate()
        val dateRangeFirst = now.withDayOfMonth(1).toString("yyyy-MM-dd")
        val dateRangeEnd = now.toString("yyyy-MM-dd", Locale.getDefault())
        Log.d("DateRange", "$dateRangeFirst")
        Log.d("DateRange", "$dateRangeEnd")
        lifecycleScope.launch {
            try {
                val response = ApiInterface.createApi().getPersentaseKaryawan(username, password, dateRangeFirst.toString(), dateRangeEnd.toString())
                if (response.isSuccessful) {
                    if (response.body()!!.code == 200) {
                        viewModel.setPercentageData(response.body()!!.persentase)
                        viewModel.setPercentageResult(response.body()!!.hasil)
                        applyFilter("Bulan", response.body()!!)
                    }
                } else {
                    Log.d("Error", response.message())
                    Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengambil data persentase", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e:Exception) {
                Log.d("Error", e.toString())
                Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengambil data persentase, silahkan hubungi tim IT", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyFilterPekan() {
        val username = LoginPreferences.username
        val password = LoginPreferences.password
        val now = LocalDate()
        val dateRangeFirst = now.withDayOfWeek(DateTimeConstants.MONDAY)
        val dateRangeEnd = now.toString("yyyy-MM-dd", Locale.getDefault())
        Log.d("DateRange", "$dateRangeFirst")
        Log.d("DateRange", "$dateRangeEnd")
        lifecycleScope.launch {
            try {
                val response = ApiInterface.createApi().getPersentaseKaryawan(username, password, dateRangeFirst.toString(), dateRangeEnd.toString())
                if (response.isSuccessful) {
                    if (response.body()!!.code == 200) {
                        viewModel.setPercentageData(response.body()!!.persentase)
                        viewModel.setPercentageResult(response.body()!!.hasil)
                        applyFilter("Pekan", response.body()!!)
                    }
                } else {
                    Log.d("Error response", response.message())
                    Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengambil data persentase", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e:Exception) {
                Log.d("Error message", e.toString())
                Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengambil data persentase", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyFilterCustom(firstDate:Long, lastDate:Long) {
        val username = LoginPreferences.username
        val password = LoginPreferences.password
        val dateRangeFirst = convertToDate(firstDate)
        val dateRangeLast = convertToDate(lastDate)

        Log.d("DateRange", "$dateRangeFirst")
        Log.d("DateRange", "$dateRangeLast")

        lifecycleScope.launch {
            try {
                val response = ApiInterface.createApi().getPersentaseKaryawan(username, password, dateRangeFirst, dateRangeLast)
                if (response.isSuccessful) {
                    if (response.body()!!.code == 200) {
                        viewModel.setPercentageData(response.body()!!.persentase)
                        viewModel.setPercentageResult(response.body()!!.hasil)
                        applyFilter("Custom", response.body()!!)

                    }
                } else {
                    Log.d("Error", response.message())
                    Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengambil data persentase", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e:Exception) {
                Log.d("Error", e.toString())
                Snackbar.make(requireActivity().findViewById(R.id.container), "Gagal mengambil data persentase, silahkan hubungi tim IT", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun applyFilter(tipeFilter:String, response: PercentageModel) {

        if (response.persentase.persentaseKehadiran == null || response.persentase.persentaseKetidakhadiran == null) {
            binding.persenkehadiran.text = "-%"
            binding.persenKetidakhadiran.text = "-%"
        } else {
            binding.attendancePercentage.isVisible = true
            binding.keteranganPersentase.isVisible = true

            binding.btnFilter.text = tipeFilter

            val splitRangeTanggal = response.rangeTanggal.split(" - ")

            binding.rangeTanggal.text = "Persentase tanggal" + "\n" + getProperNameOfDate(splitRangeTanggal[0]) + " " + "s/d" + " " + getProperNameOfDate(splitRangeTanggal[1])

            binding.persenKetidakhadiran.text = viewModel.getPercentageData()?.persentaseKetidakhadiran?.toInt().toString() + "%"
            binding.persenkehadiran.text = viewModel.getPercentageData()?.persentaseKehadiran?.toInt().toString() + "%"

            val keterlambatan = response.hasil.persentaseTelat?.toInt()
            val absenNotFull = response.hasil.persentaseTidakFullAbsen?.toInt()
            val onTime = response.hasil.persentaseOnTime?.toInt()
            val tidakHadir = response.hasil.persentaseTidakHadir?.toInt()
            val cutiAtauIzin = response.hasil.persentaseIzinAtauCuti?.toInt()
            val warnaKuning = keterlambatan!! + absenNotFull!!

            Log.d("AbsenFull", onTime.toString())
            Log.d("TidakHadir", tidakHadir.toString())
            Log.d("CutiAtauIzin", cutiAtauIzin.toString())
            Log.d("WarnaKuning", warnaKuning.toString())

            binding.txtAttended.text = onTime.toString() + "%"
            binding.txtLate.text = "$warnaKuning%"
            binding.txtPermission.text = cutiAtauIzin.toString() + "%"
            binding.absentTxt.text = tidakHadir.toString() + "%"

            binding.attendedPercentage.isVisible = onTime != 0
            binding.latePercentage.isVisible = warnaKuning != 0
            binding.permissionPercentage.isVisible = cutiAtauIzin != 0
            binding.absentPercentage.isVisible = tidakHadir != 0
        }
    }

    private fun convertToDate(timeInMilis: Long) : String {
        val date = Date(timeInMilis)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    private fun getProperNameOfDate(date:String) : String {
        val splittedDate = date.split("-")
        val month = splittedDate[1]
        val year = splittedDate[0]
        val day = splittedDate[2]

        return day + " " + getMonthName(month) + " " + year
    }

    private fun getMonthName(month : String) : String {
        return when(month) {
            "01" -> "Januari"
            "02" -> "Februari"
            "03" -> "Maret"
            "04" -> "April"
            "05" -> "Mei"
            "06" -> "Juni"
            "07" -> "Juli"
            "08" -> "Agustus"
            "09" -> "September"
            "10" -> "Oktober"
            "11" -> "November"
            "12" -> "Desember"
            else -> "-"
        }
    }

    private fun retrieveServerClock() {
        lifecycleScope.launch {
            try {
                val response = ApiInterface.createApi().getDateNow()
                if (response.isSuccessful) {
                    viewModel.setServerClock(response.body()!!.clock)
                    viewModel.setServerDate(response.body()!!.date)
                    val currentClock = response.body()!!.clock
                    binding.txtCurrentClock.text = currentClock.split(":")[0] + ":" + currentClock.split(":")[1]
                    Log.d("clock", currentClock)
                } else {
                    viewModel.setServerClock(null)
                    viewModel.setServerDate(null)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Gagal mengambil jam server")
                        .setMessage("Anda mungkin tidak bisa absen sampai jam server tersedia")
                        .setPositiveButton("Ok") { _, _ ->}
                        .create()
                        .show()
                    Log.d("Error", response.body().toString())
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }


    private fun setTextAppearance(context: Context) {
        val appearanceSettings = Preferences(context).textSize
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.txtCurrentClock.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.filter.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.kehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleSmall)
                    binding.persenkehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.ketidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleSmall)
                    binding.persenKetidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.rangeTanggal.setTextAppearance(requireContext(), TextAppearance_Material3_BodyMedium)
                }
                "normal" -> {
                    binding.txtCurrentClock.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.filter.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.kehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.persenkehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleLarge)
                    binding.ketidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.persenKetidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_TitleMedium)
                    binding.rangeTanggal.setTextAppearance(requireContext(), TextAppearance_Material3_LabelLarge)
                }
                "besar" -> {
                    binding.txtCurrentClock.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineLarge)
                    binding.filter.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineLarge)
                    binding.kehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineSmall)
                    binding.persenkehadiran.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineLarge)
                    binding.ketidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineSmall)
                    binding.persenKetidakhadiran.setTextAppearance(requireContext(), TextAppearance_Material3_HeadlineSmall)
                    binding.rangeTanggal.setTextAppearance(requireContext(), TextAppearance_Material3_BodySmall)
                }
            }
        } else {
            when (appearanceSettings) {
                "kecil" -> {
                    binding.txtCurrentClock.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.filter.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.kehadiran.setTextAppearance(TextAppearance_Material3_TitleSmall)
                    binding.persenkehadiran.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.ketidakhadiran.setTextAppearance(TextAppearance_Material3_TitleSmall)
                    binding.persenKetidakhadiran.setTextAppearance(TextAppearance_Material3_TitleMedium)
                }
                "normal" -> {
                    binding.txtCurrentClock.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.filter.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.kehadiran.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.persenkehadiran.setTextAppearance(TextAppearance_Material3_TitleLarge)
                    binding.ketidakhadiran.setTextAppearance(TextAppearance_Material3_TitleMedium)
                    binding.persenKetidakhadiran.setTextAppearance(TextAppearance_Material3_TitleLarge)
                }
                "besar" -> {
                    binding.txtCurrentClock.setTextAppearance(TextAppearance_Material3_HeadlineLarge)
                    binding.filter.setTextAppearance(TextAppearance_Material3_HeadlineLarge)
                    binding.kehadiran.setTextAppearance(TextAppearance_Material3_HeadlineSmall)
                    binding.persenkehadiran.setTextAppearance(TextAppearance_Material3_HeadlineLarge)
                    binding.ketidakhadiran.setTextAppearance(TextAppearance_Material3_HeadlineSmall)
                    binding.persenKetidakhadiran.setTextAppearance(TextAppearance_Material3_HeadlineLarge)
                }
            }
        }
    }
}