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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroutine.databinding.FragmentHomeBinding
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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.rvHomeQuestion.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = HomeListAdapter(moveToChatActivity = ::moveToChatActivity, homeViewModel)
        }
    }

    private fun initObserver() {
        homeViewModel.questionList.observe(viewLifecycleOwner) {
            (binding.rvHomeQuestion.adapter as HomeListAdapter).submitList(it)
        }

        homeViewModel.language.observe(viewLifecycleOwner) {
            (binding.rvHomeQuestion.adapter as HomeListAdapter).submitList(homeViewModel.questionList.value)
            CoroutineScope(Dispatchers.Main).launch {
                // homeViewModel.updateTranslatedQuestionList()
            }
        }

        binding.etHomeSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                homeViewModel.updateSearchedList(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                (binding.rvHomeQuestion.adapter as HomeListAdapter).submitList(homeViewModel.searchedQuestionList.value)
            }

        })
    }

    private fun initListener() {
        binding.btnHomePost.setOnClickListener {
            val intent = Intent(this.context, PostingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun moveToChatActivity(qid: String, uid: String) {
        val intent = Intent(this.context, ChatActivity::class.java)
        intent.putExtra(EXTRA_KEY_MOVETOCHAT, uid)
        intent.putExtra(EXTRA_QKEY_MOVETOCHAT, qid)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}