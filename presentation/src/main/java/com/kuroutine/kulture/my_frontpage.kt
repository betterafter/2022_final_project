package com.kuroutine.kulture

import android.os.Build.VERSION_CODES.Q
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityMainBinding


class my_frontpage : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            binding.apply{

            }

        }


}