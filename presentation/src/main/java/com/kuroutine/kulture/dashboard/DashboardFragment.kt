package com.kuroutine.kulture.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityLoginBinding
import com.example.kuroutine.databinding.FragmentDashboardBinding
import com.kuroutine.kulture.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val dashboardViewModel by viewModels<DashboardViewModel>()
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentDashboardBinding>(inflater, R.layout.fragment_dashboard, container, false).apply {
            viewModel = dashboardViewModel
            lifecycleOwner = this@DashboardFragment
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}