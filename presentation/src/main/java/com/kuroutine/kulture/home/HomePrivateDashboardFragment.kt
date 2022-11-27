package com.kuroutine.kulture.home

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroutine.databinding.FragmentHomeBinding
import com.example.kuroutine.databinding.FragmentHomePrivateDashboardBinding
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
class HomePrivateDashboardFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private var _binding: FragmentHomePrivateDashboardBinding? = null

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
        _binding = FragmentHomePrivateDashboardBinding.inflate(inflater, container, false).apply {
            viewModel = homeViewModel
            lifecycleOwner = this@HomePrivateDashboardFragment
        }
        val root: View = binding.root

        init()
        initObserver()

        var array = arrayListOf<String>()
        array.add("최신순")
        array.add("과거순")
        var adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, array)
        val spinner: Spinner = binding.spinner
        spinner.adapter = adapter
        var spinnerCurrentItem = spinner.selectedItem.toString()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (spinnerCurrentItem != spinner.selectedItem.toString()) {
                    homeViewModel.setReversedPrivateList()
                    spinnerCurrentItem = spinner.selectedItem.toString()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun init() {
        binding.rvHomeQuestion.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = HomeListAdapter(moveToChatActivity = ::moveToChatActivity, homeViewModel, parentFragmentManager)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        homeViewModel.questionList.observe(viewLifecycleOwner) {
            (binding.rvHomeQuestion.adapter as HomeListAdapter).submitList(it)
            //(binding.rvHomeQuestion.adapter as HomeListAdapter).notifyDataSetChanged()
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
            it.rvHomeQuestion.adapter?.let {
                return binding.rvHomeQuestion.adapter as HomeListAdapter
            } ?: run {
                return null
            }
        } ?: run {
            return null
        }
    }
}