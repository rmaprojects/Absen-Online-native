package com.pklproject.checkincheckout.ui.dashboard.history.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.models.History
import com.pklproject.checkincheckout.api.models.HistoryAbsenModel
import com.pklproject.checkincheckout.databinding.ItemHistoryBinding
import com.pklproject.checkincheckout.ui.dashboard.history.item.HistoryItem.HistoryItemViewHolder

class HistoryItem(private val historyModel: HistoryAbsenModel?): Adapter<HistoryItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val history = historyModel?.history?.get(position)
        holder.bindView(history)
    }

    override fun getItemCount(): Int {
        return historyModel?.history?.size ?: 1
    }

    class HistoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding:ItemHistoryBinding by viewBinding()
        fun bindView(history: History?) {
            val tanggalAbsen = history?.tanggal?: "Data Kosong"
            getProperMonth(tanggalAbsen)
            binding.jamSiang.text = history?.jamMasukSiang
            binding.jamPulang.text = history?.jamMasukPulang

            if (history?.jamMasukPagi == "Data Belum Ada") {
                binding.noDataCardView.isVisible
                !binding.cardViewData.isVisible
            } else {
                !binding.noDataCardView.isVisible
                binding.cardViewData.isVisible
            }

            if (history?.absenSiangDiperlukan == "1") {
                binding.jamSiang.isVisible
            } else {
                !binding.jamSiang.isVisible
            }

            when {
                history?.statusKeterlambatanPagi == "Terlambat" -> {
                    binding.statusIconDay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_telat, 0, 0, 0)
                    binding.jamPagi.text = history.jamMasukPagi
                }
                history?.statusKeterlambatanPagi == "Hadir" -> {
                    binding.statusIconDay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sudah_absen, 0, 0, 0)
                    binding.jamPagi.text = history.jamMasukPagi
                }
                history?.jamMasukPagi == null -> {
                    binding.jamPagi.text = "--/--"
                    binding.statusIconDay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_not_available_24, 0, 0, 0)
                }
            }

            when {
                history?.statusKeterlambatanSiang == "Terlambat" -> {
                    binding.statusIconNoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_telat, 0, 0, 0)
                    binding.jamSiang.text = history.jamMasukSiang
                }
                history?.statusKeterlambatanSiang == "Hadir" -> {
                    binding.statusIconNoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sudah_absen, 0, 0, 0)
                    binding.jamSiang.text = history.jamMasukSiang
                }
                history?.jamMasukSiang == null -> {
                    binding.jamSiang.text = "--/--"
                    binding.statusIconNoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_not_available_24, 0, 0, 0)
                }
            }

            when {
                history?.statusKeterlambatanPulang == "Terlambat" -> {
                    binding.statusIconHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_telat, 0, 0, 0)
                    binding.jamPulang.text = history.jamMasukPulang
                }
                history?.statusKeterlambatanPulang == "Hadir" -> {
                    binding.statusIconHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sudah_absen, 0, 0, 0)
                    binding.jamPulang.text = history.jamMasukPulang
                }
                history?.jamMasukPulang == null -> {
                    binding.jamPulang.text = "--/--"
                    binding.statusIconHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_not_available_24, 0, 0, 0)
                }
            }
        }

        private fun getProperMonth(tanggalAbsen: String) {
            val month = tanggalAbsen.substring(5, 7)
            val year = tanggalAbsen.substring(0, 4)
            val day = tanggalAbsen.substring(8, 10)
            val properMonth = when (month) {
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
                else -> "Data Kosong"
            }
            binding.tanggalAbsen.text = "$day $properMonth $year"
        }
    }
}