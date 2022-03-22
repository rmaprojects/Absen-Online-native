package com.pklproject.checkincheckout.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.pklproject.checkincheckout.MainActivity
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.`interface`.ApiInterface
import com.pklproject.checkincheckout.databinding.ActivityLoginBinding
import com.pklproject.checkincheckout.ui.settings.TinyDB
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val binding : ActivityLoginBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = binding.username.editText?.text
        val Password = binding.password.editText?.text
        val api = ApiInterface.createApi()

        binding.masuk.setOnClickListener {
            lifecycleScope.launch {
//                val response = api.login()

                try {
                    Snackbar.make(binding.masuk, "Login Berhasil!", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                    TinyDB(this@LoginActivity).putObject(
                        KEYSIGNIN, SignInReturn(
                            response.access_token,
                            response.expires_in,
                            response.refresh_token,
                            response.token_type,
                            response.user
                        ))

                    SignedInPreferences(this@LoginActivity).isSignedIn = true
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } catch (e:Exception) {
                    Snackbar.make(binding.masuk, "User credential salah! Silahkan perbaiki lalu coba lagi!", Snackbar.LENGTH_SHORT)
                        .setAction("Ok") {}
                        .show()
                }
                }
            }
        }

        //TODO: buat function Login
        companion object {
            const val KEYSIGNIN = "SIGNINKEY"
        }
    }

