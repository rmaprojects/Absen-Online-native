package com.pklproject.checkincheckout.ui.history.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.l4digital.fastscroll.FastScroller
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.models.apimodel.History
import com.pklproject.checkincheckout.api.models.apimodel.HistoryAbsenModel
import com.pklproject.checkincheckout.databinding.ItemHistoryBinding
import com.pklproject.checkincheckout.ui.history.item.HistoryItem.HistoryItemViewHolder

class HistoryItem(private val historyModel: HistoryAbsenModel?): Adapter<HistoryItemViewHolder>(), FastScroller.SectionIndexer {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val history = historyModel?.history?.get(position)
        holder.bindView(history)

        val tanggalAbsen = history?.tanggal?: "Data Kosong"
        holder.binding.tanggalAbsen.text = getProperMonth(tanggalAbsen)
        holder.binding.tanggalIzin.text = getProperMonth(tanggalAbsen)
    }

    override fun getItemCount(): Int {
        return historyModel?.history?.size ?: 1
    }

    class HistoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding:ItemHistoryBinding by viewBinding()
        fun bindView(history: History?) {

            if (history?.tanggal == "Data Belum Ada" || history?.tanggal == null) {
                binding.cardViewData.isVisible = false
                binding.noDataCardView.isVisible = true
                binding.izinCardView.isVisible = false
            } else {
                binding.cardViewData.isVisible = true
                binding.noDataCardView.isVisible = false
                if (history.izin == "1" || history.cuti == "1") {
                    binding.cardViewData.isVisible = false
                    binding.noDataCardView.isVisible = false
                    binding.izinCardView.isVisible = true
                } else {
                    binding.izinCardView.isVisible = false
                    binding.cardViewData.isVisible = true
                    binding.noDataCardView.isVisible = false
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

                    when (history?.absenSiangDiperlukan) {
                        "1" -> {
                            binding.jamSiang.isVisible = true
                            binding.statusIconNoon.isVisible = true
                            when (history?.statusKeterlambatanSiang) {
                                "Terlambat" -> {
                                    binding.statusIconNoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_telat, 0, 0, 0)
                                    binding.jamSiang.text = history.jamMasukSiang
                                }
                                "Hadir" -> {
                                    binding.statusIconNoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sudah_absen, 0, 0, 0)
                                    binding.jamSiang.text = history.jamMasukSiang
                                }
                                else -> {
                                    binding.jamSiang.text = "--/--"
                                    binding.statusIconNoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_not_available_24, 0, 0, 0)
                                }
                            }
                        }
                        "0" -> {
                            binding.jamSiang.isVisible = false
                            binding.statusIconNoon.isVisible = false
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
            }
        }
    }

    private fun getProperMonth(tanggalAbsen: String) : String {
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
        return "$day $properMonth $year"
    }

    override fun getSectionText(position: Int): CharSequence {
        return getProperMonth(historyModel?.history?.get(position)?.tanggal.toString())
    }
}