package com.pklproject.checkincheckout.ui.auth.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val Binding: ActivityLoginBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}