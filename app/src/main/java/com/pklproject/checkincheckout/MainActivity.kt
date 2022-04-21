package com.pklproject.checkincheckout

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.location.LocationManagerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.bulk
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.preferencesmodel.AbsenSettingsPreferences
import com.pklproject.checkincheckout.api.models.preferencesmodel.LoginPreferences
import com.pklproject.checkincheckout.api.models.preferencesmodel.ThemePreferences
import com.pklproject.checkincheckout.databinding.ActivityMainBinding
import com.pklproject.checkincheckout.notification.NotificationWorker
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.viewmodel.ServiceViewModel
import kotlinx.coroutines.launch
import mumayank.com.airlocationlibrary.AirLocation
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel:ServiceViewModel by viewModels()
    private lateinit var airLocation: AirLocation

    override fun onStart() {
        super.onStart()
        if (isLocationEnabled(this)) {
            airLocation.start()
        } else {
            MaterialAlertDialogBuilder(this)
                .setTitle("Aktifkan lokasi terlebih dahulu")
                .setMessage("Aplikasi ini tidak akan berjalan jika anda menonaktifkan GPS")
                .setPositiveButton("Aktifkan") { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("Sudah aktif") { _, _ ->

                }
                .setNeutralButton("Batal") { _, _ ->
                    finish()
                }
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkTheme()
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        Kotpref.init(this)

        val username = LoginPreferences.username
        val password = LoginPreferences.password
        retrieveSettingsAbsen()
        retrieveTodayAbsen(ApiInterface.createApi(), username, password)

        airLocation = AirLocation(this, object : AirLocation.Callback {
            override fun onSuccess(locations: ArrayList<Location>) {
                viewModel.setLatitude(locations[0].latitude)
                viewModel.setLongitude(locations[0].longitude)
            }

            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
                viewModel.setLatitude(0.0)
                viewModel.setLongitude(0.0)
                Snackbar.make(
                    binding.container,
                    "Gagal mendapatkan lokasi",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
                Log.d("Location", locationFailedEnum.toString())
            }

        }, true)

        airLocation.start()

        notificationWorker()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_absen,
                R.id.navigation_history,
                R.id.navigation_settings,
                R.id.navigation_profile
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            var showBottomNav: Boolean
            var showTopAppBar = true
            var showImage: Boolean
            var showTitle = ""
            when (destination.id) {
                R.id.navigation_dashboard -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showImage = true
                    showTitle = Preferences(this).employeeName.toString()
                    binding.fragmentContainer.setPadding(0, 0, 0, 128)
                }
                R.id.navigation_absen -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showImage = false
                    binding.toolBar.subtitle = null
                    showTitle = "Absen"
                    binding.fragmentContainer.setPadding(0, 0, 0, 128)
                    retrieveTodayAbsen(ApiInterface.createApi(), username, password)
                }
                R.id.navigation_history -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showImage = false
                    binding.toolBar.subtitle = null
                    showTitle = "History"
                    binding.fragmentContainer.setPadding(0, 0, 0, 128)
                }
                R.id.navigation_settings -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showImage = false
                    binding.toolBar.subtitle = null
                    showTitle = "Pengaturan"
                    binding.fragmentContainer.setPadding(0, 0, 0, 128)
                }
                R.id.navigation_profile -> {
                    showBottomNav = true
                    showTopAppBar = true
                    showImage = false
                    binding.toolBar.subtitle = null
                    showTitle = "Profile"
                    binding.fragmentContainer.setPadding(0, 0, 0, 128)
                }
                else -> {
                    showImage = false
                    showBottomNav = false
                    binding.fragmentContainer.setPadding(0, 0, 0, 0)
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

    private fun notificationWorker() {

        val waktuAkhirAbsenPagi = AbsenSettingsPreferences.absenPagiAkhir
        Log.d("waktuAkhirAbsenPagi", waktuAkhirAbsenPagi)
        val waktuAkhirAbsenSiang = AbsenSettingsPreferences.absenSiangAkhir
        Log.d("waktuAkhirAbsenSiang", waktuAkhirAbsenSiang)
        val waktuAkhirAbsenPulang = AbsenSettingsPreferences.absenPulangAkhir
        Log.d("waktuAkhirAbsenPulang", waktuAkhirAbsenPulang)

        val workerPagi = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInputData(workDataOf(
                "jam" to waktuAkhirAbsenPagi,
                "tipeAbsen" to 0
            ))
            .build()

        val workerSiang = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInputData(workDataOf(
                "jam" to waktuAkhirAbsenSiang,
                "tipeAbsen" to 1
            ))
            .build()

        val workerPulang = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInputData(workDataOf(
                "jam" to waktuAkhirAbsenPulang,
                "tipeAbsen" to 2
            ))
            .build()

        WorkManager.getInstance(this).enqueue(workerPagi)
        WorkManager.getInstance(this).enqueue(workerSiang)
        WorkManager.getInstance(this).enqueue(workerPulang)
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

    fun retrieveSettingsAbsen() {
        val api = ApiInterface.createApi()
        lifecycleScope.launch {
            try {
                val response = api.getAbsenSettings()
                if (response.status) {
                    AbsenSettingsPreferences.bulk {
                        this.absenPagiAkhir = response.setting.absenPagiAkhir
                        this.absenPagiAwal = response.setting.absenPagiAwal
                        this.absenSiangAkhir = response.setting.absenSiangAkhir
                        this.absenSiangAwal = response.setting.absenSiangAwal
                        this.absenPulangAkhir = response.setting.absenPulangAkhir
                        this.absenPulangAwal = response.setting.absenPulangAwal
                        this.absenSiangDiperlukan = response.setting.absenSiangDiperlukan
                    }
                } else {
                    Snackbar.make(binding.root, response.message, Snackbar.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun checkTheme() {
        when (ThemePreferences.theme) {
            0 -> setDefaultNightMode(MODE_NIGHT_NO)
            1 -> setDefaultNightMode(MODE_NIGHT_YES)
            else -> setDefaultNightMode(MODE_NIGHT_NO)
        }
    }

    private fun retrieveTodayAbsen(api:ApiInterface, username:String, password:String) {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        lifecycleScope.launch {
            try {
                val response = api.cekAbsenHariIni(username, password, todayDate)
                if (response.code == 200) {
                    viewModel.setTodayAttendance(response.absenHariIni)
                } else {
                    viewModel.setTodayAttendance(null)
                }
            } catch (e:Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLocation.onActivityResult(
            requestCode,
            resultCode,
            data
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    companion object {
        const val PENGATURANABSENKEY = "ABSENSIKEYSETTINGVALUE"
    }

}