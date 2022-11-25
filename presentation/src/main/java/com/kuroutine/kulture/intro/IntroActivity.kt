package com.kuroutine.kulture.intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.kuroutine.kulture.EXTRA_KEY_ISPRIVATE
import com.kuroutine.kulture.EXTRA_KEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_KEY_USERS
import com.kuroutine.kulture.EXTRA_QKEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_SHOULD_MOVE_TO_SPECIFIC_PAGE
import com.kuroutine.kulture.MainActivity
import com.kuroutine.kulture.PAGE_CHAT
import com.kuroutine.kulture.chat.ChatActivity
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
    }

    override fun onResume() {
        super.onResume()
        val moveToPage = intent.getStringExtra(EXTRA_SHOULD_MOVE_TO_SPECIFIC_PAGE)

        Log.d("[keykat]", "moveToPage::: $moveToPage")

        if (moveToPage == PAGE_CHAT) {
            val intent = Intent(this, ChatActivity::class.java)

            val qid = intent.getStringExtra(EXTRA_QKEY_MOVETOCHAT)
            val uid = intent.getStringExtra(EXTRA_KEY_MOVETOCHAT)
            val users = intent.getStringArrayExtra(EXTRA_KEY_USERS)
            val isPrivate = intent.getBooleanExtra(EXTRA_KEY_ISPRIVATE, true)

            intent.putExtra(EXTRA_QKEY_MOVETOCHAT, qid)
            intent.putExtra(EXTRA_KEY_MOVETOCHAT, uid)
            intent.putExtra(EXTRA_KEY_USERS, users)
            intent.putExtra(EXTRA_KEY_ISPRIVATE, isPrivate)

            startActivity(intent)
        } else {
            initObserver()
        }
    }

    private fun initObserver() {
        introViewModel.prepareState.observe(this) {
            //if (it != null && it) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            //}
        }
    }
}