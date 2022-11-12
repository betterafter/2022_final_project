package com.kuroutine.kulture.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kuroutine.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kuroutine.kulture.EXTRA_KEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_QKEY_MOVETOCHAT
import com.kuroutine.kulture.chat.ChatActivity
import com.kuroutine.kulture.posting.PostingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var homePrivateDashboardFragment: HomePrivateDashboardFragment
    private lateinit var homePublicDashboardFragment: HomePublicDashboardFragment

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homePrivateDashboardFragment = HomePrivateDashboardFragment()
        homePublicDashboardFragment = HomePublicDashboardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewModel = homeViewModel
            lifecycleOwner = this@HomeFragment
        }
        val root: View = binding.root

        initFragment()
        init()
        initListener()
        initObserver()

        return root
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            homeViewModel.getLanguage()
        }

        CoroutineScope(Dispatchers.IO).launch {
            homeViewModel.getQuestions()
        }
    }

    private fun init() {
        CoroutineScope(Dispatchers.Main).launch {
            homeViewModel.getCurrentUser()
        }

        binding.vpHomeDashboard.apply {
            adapter = HomeViewPager2Adapter(this@HomeFragment, fragments)
        }

        TabLayoutMediator(binding.tlHomeChatType, binding.vpHomeDashboard) { tab, position ->
            if (position == 0) {
                tab.text = "공개 질문"
            } else {
                tab.text = "1:1 질문"
            }
        }.attach()
    }

    private fun initFragment() {
        fragments = ArrayList()
        fragments.add(homePublicDashboardFragment)
        fragments.add(homePrivateDashboardFragment)
    }

    private fun initObserver() {
        binding.etHomeSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                homeViewModel.updateSearchedList(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                homePrivateDashboardFragment.getAdapter()?.submitList(homeViewModel.searchedQuestionList.value)
            }

        })
    }

    private fun initListener() {
        binding.btnHomePost.setOnClickListener {
            val intent = Intent(this.context, PostingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}