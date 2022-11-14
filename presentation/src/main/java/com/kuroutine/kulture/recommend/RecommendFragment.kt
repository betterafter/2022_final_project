package com.kuroutine.kulture.recommend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.databinding.FragmentRecommendBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendFragment: Fragment() {
    var videolist : MutableList<YouTubePlayerView> = mutableListOf()
    var videourl : ArrayList<String> = ArrayList()
    lateinit var recyclerView: RecyclerView

    private var _binding: FragmentRecommendBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@RecommendFragment
        }
        val root: View = binding.root

        init()

        return root
    }

    private fun init() {
        ManageVideoUrl()

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        for (i in 0..9) {
            val num = (0..24).random()
            Log.d("[keykat]", "list:::: ${videolist.size}")
            lifecycle.addObserver(videolist.get(num))
            videolist.get(num).addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    val videoId = videourl.get(num)
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        binding.fragmentRecommendRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        val adapter = Fragment_recommend_VideoAdapter(videolist)
        binding.fragmentRecommendRecyclerView.adapter = adapter
    }

    private fun ManageVideoUrl() {
        videourl.add("fCO7f0SmrDc")
        videourl.add("FqmYc62HUec")
        videourl.add("c6ASQOwKkhk")
        videourl.add("wqGr9_zh0S0")
        videourl.add("sHqFqWDviBg")
        videourl.add("GiujAOKS4Bg")
        videourl.add("P2FcQvCbDWg")
        videourl.add("PJIDqwvg2yo")
        videourl.add("8b2DjJ5iVgI")
        videourl.add("lEqjUJuG96o")
        videourl.add("PJIDqwvg2yo")
        videourl.add("dYRITmpFbJ4")
        videourl.add("f6YDKF0LVWw")
        videourl.add("fE2h3lGlOsk")
        videourl.add("js1CtxSY38I")
        videourl.add("6uJf2IT2Zh8")
        videourl.add("Y8JFxS1HlDo")
        videourl.add("VOmIplFAGeg")
        videourl.add("pyf8cbqyfPs")
        videourl.add("uR8Mrt1IpXg")
        videourl.add("J_CFBjAyPWE")
        videourl.add("vecSVX1QYbQ")
        videourl.add("WMweEpGlu_U")
        videourl.add("4HG_CJzyX6A")
        videourl.add("4TWR90KJl84") //25
        for (i in videourl) {
            videolist.add(YouTubePlayerView(requireContext()))
        }
    }
}