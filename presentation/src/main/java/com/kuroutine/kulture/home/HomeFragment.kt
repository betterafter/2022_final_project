package com.kuroutine.kulture.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroutine.databinding.FragmentHomeBinding
import com.kuroutine.kulture.chat.PrivateChatAdapter
import com.kuroutine.kulture.posting.PostingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        init()
        initListener()
        initObserver()

        return root
    }

    override fun onResume() {
        super.onResume()

        homeViewModel.getQuestions()
    }

    private fun init() {
        binding.rvHomeQuestion.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = HomeListAdapter()
        }
    }

    private fun initObserver() {
        homeViewModel.questionList.observe(viewLifecycleOwner) {
            Log.d("[keykat]", "question List::: $it")
            (binding.rvHomeQuestion.adapter as HomeListAdapter).submitList(it)
        }
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