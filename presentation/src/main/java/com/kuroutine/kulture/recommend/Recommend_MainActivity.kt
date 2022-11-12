package com.kuroutine.kulture.recommend

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class Recommend_MainActivity : AppCompatActivity() {

        var videolist : ArrayList<YouTubePlayerView> = ArrayList()
        var videourl : ArrayList<String> = ArrayList()
        lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.kuroutine.R.layout.fragment_recommend)

        videourl.add("https://www.youtube.com/watch?v=fCO7f0SmrDc")
        videourl.add("https://www.youtube.com/watch?v=FqmYc62HUec&list=PL4fGSI1pDJn5S09aId3dUGp40ygUqmPGc&index=3")
        videourl.add("https://www.youtube.com/watch?v=c6ASQOwKkhk&list=PL4fGSI1pDJn5S09aId3dUGp40ygUqmPGc&index=2")
        videourl.add("https://www.youtube.com/watch?v=wqGr9_zh0S0&list=PL4fGSI1pDJn5S09aId3dUGp40ygUqmPGc&index=7")
        videourl.add("https://www.youtube.com/watch?v=sHqFqWDviBg&list=PL4fGSI1pDJn5S09aId3dUGp40ygUqmPGc&index=5")
        videourl.add("https://www.youtube.com/watch?v=GiujAOKS4Bg&list=PL4fGSI1pDJn5S09aId3dUGp40ygUqmPGc&index=18")
        videourl.add("https://www.youtube.com/watch?v=P2FcQvCbDWg&list=PL4fGSI1pDJn5S09aId3dUGp40ygUqmPGc&index=11")
        videourl.add("https://www.youtube.com/watch?v=PJIDqwvg2yo")
        videourl.add("https://www.youtube.com/watch?v=8b2DjJ5iVgI")
        videourl.add("https://www.youtube.com/watch?v=lEqjUJuG96o")

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        for (i in 0..9) {
            lifecycle.addObserver(videolist.get(i))
            videolist.get(i).addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    val videoId = videourl.get(i)
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        val recyclerView = findViewById<RecyclerView>(R.id.fragment_recommend_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        val adapter = Fragment_recommend_VideoAdapter(videolist)
        recyclerView.adapter = adapter
    }


    }




