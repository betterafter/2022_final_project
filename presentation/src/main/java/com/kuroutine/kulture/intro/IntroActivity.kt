package com.kuroutine.kulture.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityIntroBinding
import com.example.kuroutine.databinding.ActivityMainBinding
import com.kuroutine.kulture.MainActivity
import com.kuroutine.kulture.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private val introViewModel by viewModels<IntroViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        binding.viewModel = introViewModel
        binding.lifecycleOwner = this

        introViewModel.prepare()
        initObserver()
    }

    private fun initObserver() {
        introViewModel.prepareState.observe(this) {
            if (it != null && it) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
    }
}