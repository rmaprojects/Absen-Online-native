package com.pklproject.checkincheckout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.databinding.ActivitySplashScreenBinding
import com.pklproject.checkincheckout.ui.auth.LoginActivity
import com.pklproject.checkincheckout.ui.settings.Preferences

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (Preferences(this).isLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}