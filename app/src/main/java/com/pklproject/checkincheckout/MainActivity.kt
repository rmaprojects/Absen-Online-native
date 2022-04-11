package com.pklproject.checkincheckout

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.api.models.Setting
import com.pklproject.checkincheckout.databinding.ActivityMainBinding
import com.pklproject.checkincheckout.notification.NotificationWorker
import com.pklproject.checkincheckout.ui.settings.Preferences
import com.pklproject.checkincheckout.ui.settings.TinyDB
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkTheme()
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        notificationWorkerPagi()
        notificationWorkerSiang()
        notificationWorkerPulang()

        val tinyDb = TinyDB(this)
        retrieveSettingsAbsen(tinyDb)
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

    private fun notificationWorkerPagi() {

        val waktuAkhirAbsenPagi =
            TinyDB(this).getObject(PENGATURANABSENKEY, Setting::class.java).absenPagiAkhir

        val tahunIni = Calendar.getInstance().get(Calendar.YEAR)
        val bulanIni = Calendar.getInstance().get(Calendar.MONTH)
        val jamPagiAkhir = waktuAkhirAbsenPagi.split(":")[0].toInt()
        val menitPagiAkhir = waktuAkhirAbsenPagi.split(":")[1].toInt() - 10
        val hariIni = Calendar.getInstance()

        val calendarJamPagiAkhir = Calendar.getInstance()
        calendarJamPagiAkhir.set(
            tahunIni,
            bulanIni,
            hariIni.get(Calendar.DAY_OF_MONTH),
            jamPagiAkhir,
            menitPagiAkhir
        )

        val delay = calendarJamPagiAkhir.timeInMillis - hariIni.timeInMillis

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "jam" to waktuAkhirAbsenPagi,
                    "tipeAbsen" to 0
                )
            )
            .build()

        WorkManager.getInstance(this).enqueue(request)
    }

    private fun notificationWorkerSiang() {

        val waktuAkhirAbsenSiang =
            TinyDB(this).getObject(PENGATURANABSENKEY, Setting::class.java).absenSiangAkhir

        val tahunIni = Calendar.getInstance().get(Calendar.YEAR)
        val bulanIni = Calendar.getInstance().get(Calendar.MONTH)
        val jamSiangAkhir = waktuAkhirAbsenSiang.split(":")[0].toInt()
        val menitSiangAkhir = waktuAkhirAbsenSiang.split(":")[1].toInt() - 10
        val hariIni = Calendar.getInstance()

        val calendarJamSiangAKhir = Calendar.getInstance()
        calendarJamSiangAKhir.set(
            tahunIni,
            bulanIni,
            hariIni.get(Calendar.DAY_OF_MONTH),
            jamSiangAkhir,
            menitSiangAkhir
        )

        val delay = calendarJamSiangAKhir.timeInMillis - hariIni.timeInMillis

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "jam" to waktuAkhirAbsenSiang,
                    "tipeAbsen" to 1
                )
            )
            .build()

        WorkManager.getInstance(this).enqueue(request)
    }

    private fun notificationWorkerPulang() {

        val waktuAkhirAbsenPulang =
            TinyDB(this).getObject(PENGATURANABSENKEY, Setting::class.java).absenPulangAkhir
        Log.d("waktuAkhirAbsenMalam", waktuAkhirAbsenPulang)

        val tahunIni = Calendar.getInstance().get(Calendar.YEAR)
        val bulanIni = Calendar.getInstance().get(Calendar.MONTH)
        val jamPulangAkhir = waktuAkhirAbsenPulang.split(":")[0].toInt()
        val menitPulangAkhir = waktuAkhirAbsenPulang.split(":")[1].toInt() - 10
        val hariIni = Calendar.getInstance()

        val calendarJamPulangAkhir = Calendar.getInstance()
        calendarJamPulangAkhir.set(
            tahunIni,
            bulanIni,
            hariIni.get(Calendar.DAY_OF_MONTH),
            jamPulangAkhir,
            menitPulangAkhir
        )

        val delay = calendarJamPulangAkhir.timeInMillis - hariIni.timeInMillis

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "jam" to waktuAkhirAbsenPulang,
                    "tipeAbsen" to 2
                )
            )
            .build()

        WorkManager.getInstance(this).enqueue(request)
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

    fun retrieveSettingsAbsen(tinyDB: TinyDB) {
        val api = ApiInterface.createApi()
        lifecycleScope.launch {
            try {
                val response = api.getAbsenSettings()
                if (response.status) {
                    tinyDB.putObject(PENGATURANABSENKEY, response.setting)
                } else {
                    Snackbar.make(binding.root, response.message, Snackbar.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun checkTheme() {
        when (Preferences(this).changeTheme) {
            0 -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            1 -> setDefaultNightMode(MODE_NIGHT_NO)
            2 -> setDefaultNightMode(MODE_NIGHT_YES)
        }
    }

    companion object {
        const val PENGATURANABSENKEY = "ABSENSIKEYSETTINGVALUE"
    }

}