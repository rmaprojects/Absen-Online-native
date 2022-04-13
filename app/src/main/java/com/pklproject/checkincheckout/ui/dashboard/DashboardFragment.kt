package com.pklproject.checkincheckout.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val binding: FragmentDashboardBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentClock = SimpleDateFormat("hh:mm", Locale.getDefault()).format(Date())

        binding.txtCurrentClock.text = currentClock
    }
}