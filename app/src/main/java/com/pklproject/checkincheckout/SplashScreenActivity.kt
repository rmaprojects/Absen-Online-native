package com.pklproject.checkincheckout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.databinding.ActivitySplashScreenBinding
import com.pklproject.checkincheckout.ui.auth.login.LoginActivity
import com.pklproject.checkincheckout.ui.settings.TinyDB

class SplashScreenActivity : AppCompatActivity() {

    private val binding: ActivitySplashScreenBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        TinyDB(this).putBoolean("isLFirstRun", true)
        if (TinyDB(this).getBoolean("isFirstRun")) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}