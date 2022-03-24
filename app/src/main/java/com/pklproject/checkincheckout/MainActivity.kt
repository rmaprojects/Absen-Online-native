package com.pklproject.checkincheckout

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.api.models.LoginModel
import com.pklproject.checkincheckout.databinding.ActivityMainBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.ui.settings.TinyDB

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding:ActivityMainBinding by viewBinding()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        val tinyDb = TinyDB(this)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_absen, R.id.navigation_history, R.id.navigation_settings, R.id.navigation_profile
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            var showBottomNav = true
            var showTopAppBar = true
            var showImage:Boolean
            var showTitle = ""
            when (destination.id) {
                R.id.navigation_dashboard -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showImage = true
                    showTitle = Preferences(this).employeeName.toString()
                    binding.fragmentContainer.setPadding(0,0,0,128)
                }
                R.id.navigation_absen -> {
                    showBottomNav = true
                    showTopAppBar =  true
                    showImage =  false
                    binding.toolBar.subtitle = null
                    showTitle = "Absen"
                    binding.fragmentContainer.setPadding(0,0,0,128)
                }
                R.id.navigation_history -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showImage =  false
                    binding.toolBar.subtitle = null
                    showTitle = "History"
                    binding.fragmentContainer.setPadding(0,0,0,128)
                }
                R.id.navigation_settings -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showImage =  false
                    binding.toolBar.subtitle = null
                    showTitle = "Pengaturan"
                    binding.fragmentContainer.setPadding(0,0,0,128)
                }
                R.id.navigation_profile -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showImage =  false
                    binding.toolBar.subtitle = null
                    showTitle = "Profile"
                    binding.fragmentContainer.setPadding(0,0,0,128)
                }
                else -> {
                    showImage = false
                    showBottomNav = false
                    binding.fragmentContainer.setPadding(0,0,0,0)
                }
            }
            binding.bottomNavView.isVisible = showBottomNav
            binding.toolBar.isVisible = showTopAppBar
            binding.profilePhoto.isVisible = showImage
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