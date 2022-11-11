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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroutine.databinding.FragmentHomeBinding
import com.example.kuroutine.databinding.FragmentHomePrivateDashboardBinding
import com.example.kuroutine.databinding.FragmentHomePublicDashboardBinding
import com.kuroutine.kulture.EXTRA_KEY_ISPRIVATE
import com.kuroutine.kulture.EXTRA_KEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_QKEY_MOVETOCHAT
import com.kuroutine.kulture.chat.ChatActivity
import com.kuroutine.kulture.posting.PostingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomePublicDashboardFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private var _binding: FragmentHomePublicDashboardBinding? = null

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
        _binding = FragmentHomePublicDashboardBinding.inflate(inflater, container, false).apply {
            viewModel = homeViewModel
            lifecycleOwner = this@HomePublicDashboardFragment
        }
        val root: View = binding.root

        init()
        initObserver()

        return root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun init() {
        binding.rvHomePublicQuestion.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = HomeListAdapter(moveToChatActivity = ::moveToChatActivity, homeViewModel, parentFragmentManager)
        }
    }

    private fun initObserver() {
        homeViewModel.publicQuestionList.observe(viewLifecycleOwner) {
            (binding.rvHomePublicQuestion.adapter as HomeListAdapter).submitList(it)
        }

        homeViewModel.language.observe(viewLifecycleOwner) {
            (binding.rvHomePublicQuestion.adapter as HomeListAdapter).submitList(homeViewModel.publicQuestionList.value)
            CoroutineScope(Dispatchers.Main).launch {
                // homeViewModel.updateTranslatedQuestionList()
            }
        }
    }

    private fun moveToChatActivity(qid: String, uid: String, isPrivate: Boolean) {
        val intent = Intent(this.context, ChatActivity::class.java)
        intent.putExtra(EXTRA_KEY_MOVETOCHAT, uid)
        intent.putExtra(EXTRA_QKEY_MOVETOCHAT, qid)
        intent.putExtra(EXTRA_KEY_ISPRIVATE, isPrivate)
        startActivity(intent)
    }

    fun getAdapter(): HomeListAdapter? {
        _binding?.let {
            it.rvHomePublicQuestion.adapter?.let {
                return binding.rvHomePublicQuestion.adapter as HomeListAdapter
            } ?: run {
                return null
            }
        } ?: run {
            return null
        }
    }
}