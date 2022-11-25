package com.kuroutine.kulture.login

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityLoginBinding
import com.facebook.login.LoginManager
import com.kuroutine.kulture.GOOGLE_SIGNIN_RESULT_CODE
import com.kuroutine.kulture.MainActivity
import dagger.hilt.android.AndroidEntryPoint


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

        loginViewModel.checkAutoLogin()

        initObserver()
        initListener()

        binding.ivLoginApplogo1.setColorFilter(ContextCompat.getColor(this@LoginActivity, R.color.white))
        Glide.with(applicationContext)
            .load(R.drawable.gif_global2)
            .into(binding.ivLoginApplogo1)
    }

    // view와 결합성이 커서 나누지 못할 경우 사용
    private fun initObserver() {
        loginViewModel.googleSingInIntent.observe(this) { googleIntent ->
            if (googleIntent != null) {
                startActivityForResult(googleIntent, GOOGLE_SIGNIN_RESULT_CODE)
            }
        }

        loginViewModel.currentUser.observe(this) { user ->
            if (user != null) {
                // TODO: 이동 로직 만들 것
                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
    }

    // view와 결합성이 커서 나누지 못할 경우 사용
    // xml의 onClick이 먹지 않는 경우 사용 (ex.구글 로그인 버튼)
    private fun initListener() {
        binding.btnLoginGoogle.setOnClickListener {
            googleLogin()
        }
    }

    private fun googleLogin() {
        loginViewModel.getGoogleSignInIntent()
    }

    fun facebookLogin(view: View) {
        LoginManager.getInstance().logInWithReadPermissions(
            this@LoginActivity,
            loginViewModel.permissionList
        )
        loginViewModel.loginWithFacebook()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // google 로그인일 경우
        if (requestCode == GOOGLE_SIGNIN_RESULT_CODE) {
            loginViewModel.googleLogin(data)
        }

        // 그 외의 경우 (지금은 facebook만)
        else {
            binding.viewModel?.callbackManagerOnActivityResult(requestCode, resultCode, data)
        }
    }
}