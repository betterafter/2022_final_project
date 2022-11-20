package com.kuroutine.kulture

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityMainBinding
import com.kuroutine.kulture.recommend.RecommendFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_ranking, R.id.navigation_recommend,
                R.id.navigation_chat, R.id.navigation_myPage
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    interface OnBackPressedListener {
        fun onBackPressed(): Boolean
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        val fragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment
        if (fragment is OnBackPressedListener) {
            if ((fragment as OnBackPressedListener).onBackPressed()) {
                return
            } else {
                super.onBackPressed()
            }
            return
        } else {
            super.onBackPressed()
        }
    }
}