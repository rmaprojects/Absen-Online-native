package com.pklproject.checkincheckout.ui.settings

import android.app.AppComponentFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tinyDB = TinyDB(requireContext())
        val isDarkMode = tinyDB.getBoolean(SETTING_TEMA)

        binding.switchTema.isChecked = isDarkMode

        binding.switchTema.setOnCheckedChangeListener{  _, isChecked->
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            tinyDB.putBoolean(SETTING_TEMA, isChecked)
        }
    }

    companion object{
        const val SETTING_TEMA = "SETTIG_MOD"

    }
}