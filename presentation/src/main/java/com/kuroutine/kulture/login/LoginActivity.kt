package com.kuroutine.kulture.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityLoginBinding?>(this, R.layout.activity_login).apply {
            viewModel = loginViewModel
            lifecycleOwner = this@LoginActivity
        }
    }

    fun facebookLogin(view: View) {
        LoginManager.getInstance().logInWithReadPermissions(this@LoginActivity,
            loginViewModel.permissionList)
        loginViewModel.loginWithFacebook()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.viewModel?.callbackManagerOnActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}