package com.kuroutine.kulture.recommend

import java.util.Random
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class Temp_Recommend_MainActivity : AppCompatActivity() {

    var videolist : ArrayList<YouTubePlayerView> = ArrayList()
    var videourl : ArrayList<String> = ArrayList()
    lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recommend)

        ManageVideoUrl()

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        for (i in 0..9) {
            val num = (0..24).random()
            lifecycle.addObserver(videolist.get(num))
            videolist.get(num).addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    val videoId = videourl.get(num)
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

    private fun ManageVideoUrl() {
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
        videourl.add("https://www.youtube.com/watch?v=PJIDqwvg2yo")
        videourl.add("https://www.youtube.com/watch?v=dYRITmpFbJ4")
        videourl.add("https://www.youtube.com/watch?v=f6YDKF0LVWw")
        videourl.add("https://www.youtube.com/watch?v=fE2h3lGlOsk")
        videourl.add("https://www.youtube.com/watch?v=js1CtxSY38I")
        videourl.add("https://www.youtube.com/watch?v=6uJf2IT2Zh8")
        videourl.add("https://www.youtube.com/watch?v=Y8JFxS1HlDo")
        videourl.add("https://www.youtube.com/watch?v=VOmIplFAGeg")
        videourl.add("https://www.youtube.com/watch?v=pyf8cbqyfPs")
        videourl.add("https://www.youtube.com/watch?v=uR8Mrt1IpXg")
        videourl.add("https://www.youtube.com/watch?v=J_CFBjAyPWE")
        videourl.add("https://www.youtube.com/watch?v=vecSVX1QYbQ")
        videourl.add("https://www.youtube.com/watch?v=WMweEpGlu_U")
        videourl.add("https://www.youtube.com/watch?v=4HG_CJzyX6A")
        videourl.add("https://www.youtube.com/watch?v=4TWR90KJl84") //25


    }
}
