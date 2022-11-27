package com.kuroutine.kulture.ranking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.UserModel
import com.example.kuroutine.R
import com.kuroutine.kulture.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RankingFragment : Fragment() {

    private val rankingViewModel by viewModels<RankingViewModel>()

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RankingViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ranking, container, false)

        recyclerView = view.findViewById(R.id.ranking_recyclerview)

        init()
        initObserver()

        return view
    }

    private fun init() {
        CoroutineScope(Dispatchers.Main).launch {
            rankingViewModel.getUsers()
        }

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        adapter = RankingViewAdapter()
        recyclerView.adapter = adapter
    }

    private fun initObserver() {
        rankingViewModel.userList.observe(this) {
            it?.let { list -> (recyclerView.adapter as RankingViewAdapter).submitList(list) }
        }
    }
}