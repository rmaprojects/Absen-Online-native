package com.pklproject.checkincheckout.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.FragmentDashboardBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.TinyDB

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val binding: FragmentDashboardBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}