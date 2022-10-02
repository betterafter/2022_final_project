package com.kuroutine.kulture.login

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.btnLoginFacebook.registerCallback(
            CallbackManager.Factory.create(),
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("[keykat]", "cancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("[keykat]", "$error")
                }

                override fun onSuccess(result: LoginResult) {
                    Log.d("[keykat]", "success")
                }

            }
        )
    }
}