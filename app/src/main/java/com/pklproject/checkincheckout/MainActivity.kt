package com.pklproject.checkincheckout

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding:ActivityMainBinding by viewBinding()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_profile, R.id.navigation_history, R.id.navigation_settings
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            var showBottomNav = true
            var showTopAppBar = true
            var showSubtitle = ""
            var showTitle = ""
            when (destination.id) {
                //Ketika navigation nya kesini, apakah di show atau di hide
                //Kalau di hide = false,
                //Kalau di show = true
                //Kecuali kalau subtitle, pakainya null saja
                    //TODO: isi Subtitle dan Title
                R.id.navigation_dashboard -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showSubtitle = "Nama Orang"
                }
                R.id.navigation_absen -> {
                    showBottomNav = false
                    showTopAppBar =  false
                    showSubtitle = "Absen Pagi"
                }
                R.id.navigation_history -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showSubtitle = "Riwayat"
                }
                R.id.navigation_settings -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showSubtitle = "Pengaturan"
                }
                R.id.navigation_profile -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showSubtitle = "profil"
                }
            }
            binding.bottomNavView.isVisible = showBottomNav
            binding.toolBar.isVisible = showTopAppBar
            binding.toolBar.subtitle = showSubtitle
            binding.toolBar.title = showTitle
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavView.setupWithNavController(navController)
    }

    //Suport for back button
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //For toolBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

}