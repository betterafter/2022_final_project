package com.kuroutine.kulture.ranking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    var ranking_data:ArrayList<RankingData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ranking, container, false)

        initData()
        sortData()

        CoroutineScope(Dispatchers.Main).launch {
            rankingViewModel.getUsers()
        }

        recyclerView = view.findViewById(R.id.ranking_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        adapter = RankingViewAdapter(ranking_data)
        recyclerView.adapter = adapter

        return view
    }


    private fun initData() {

        ranking_data.add(RankingData(R.drawable.img1, "user1", 15, true))
        ranking_data.add(RankingData(R.drawable.img2, "user2", 10, true))
        ranking_data.add(RankingData(R.drawable.img1, "user3", 30, false))
        ranking_data.add(RankingData(R.drawable.img2, "user4", 20, true))
        ranking_data.add(RankingData(R.drawable.img1, "user5", 25, true))
        ranking_data.add(RankingData(R.drawable.img2, "user6", 20, false))
        ranking_data.add(RankingData(R.drawable.img1, "user7", 27, true))

    }

    private fun sortData() {

        // 비공개한 user 제외
        ranking_data = ranking_data!!.filter{ it.isOpen == true } as ArrayList<RankingData>
        // level 기준 내림차순 정렬
        ranking_data.sortWith(compareBy<RankingData> {-it.level})

    }

}