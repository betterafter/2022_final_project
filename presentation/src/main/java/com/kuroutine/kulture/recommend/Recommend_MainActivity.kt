package com.kuroutine.kulture.recommend

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.io.FileOutputStream


class Recommend_MainActivity : AppCompatActivity() {

        var videolist : ArrayList<YouTubePlayerView> = ArrayList()
        var videourl : ArrayList<String> = ArrayList()
        var listenedMusicList : ArrayList<String> = ArrayList()
        var recommendDbhelper: Recommend_DBHelper = Recommend_DBHelper(this)
        var readableDatabase : SQLiteDatabase = recommendDbhelper.readableDatabase
        lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recommend)
        initDB()
        initListenedMusicList()

        // 아티스트 칼럼에서 아티스트 이름 뽑아오기
        val strsql = "SELECT "+"아티스트명"+"FROM music_list"
        val cursor = readableDatabase.rawQuery(strsql,null)
        val artist : ArrayList<String> = ArrayList()

        for (i in 0 until cursor.count) {
            val songname = cursor.getString(i)
            artist.add(songname)
            cursor.moveToNext() //이걸 해줘야 다음 레코드로 넘어가게된다.
        }
        cursor.close()
        //

        val tfidf_matrix = VectorSpaceModel(artist)
        val cosine_sim = cosine_similarity(tfidf_matrix, tfidf_matrix)

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

    private fun initListenedMusicList() {
        listenedMusicList.add("신촌을 못가")
        listenedMusicList.add("DNA")
        listenedMusicList.add("겨울소리")
        listenedMusicList.add("오르막길")
        listenedMusicList.add("퇴근버스")
        listenedMusicList.add("이 소설의 끝을 다시 써보려 해")
        listenedMusicList.add("소주 한잔")
        listenedMusicList.add("V")
    }


    //# 이는 1698개의 각 문서 벡터(가수 벡터)와 자기 자신을 포함한
    //# 1698개의 문서 벡터 간의 유사도가 기록된 행렬입니다.
    //# 모든 1698개 가수의 상호 유사도가 기록되어져 있습니다.
    private fun cosine_similarity(tfidfMatrix: Any, tfidfMatrix1: Any): Any {

    }

    //Returning TF-IDF Matrix
    private fun Make_TFIDF_Matrix(artist: ArrayList<String>): Any {

        
    }

    fun tf(list : List<String>,word : String) : Double {
        var result : Double = 0.0
        val forlist = list
        for( targetWord : forlist)
            if(word.equals(targetWord, ignoreCase = true))
                result ++
        return result / list.size()
    }

    fun idf(lists : List<List<String>>, word : String) : Double {
        var n : Double = 0.0
        for( list:List<String> : lists){
            for (targetword: String : list){
            if (word.equals(targetWord, ignoreCase = true)){
                n++
            break}
        }
        }
        return Math.log(lists.size() / n)
    }

    fun ifidf(list : List<String>, lists : List<List<String>>, word : String):Double{
        return tf(list,word) * idf(lists,word)
    }


    //음악 리스트 데이터베이스를 초기화
    private fun initDB() {
        val dbfile = getDatabasePath("music_list.db")
        if(dbfile.parentFile.exists())
        {
            dbfile.parentFile.mkdir()
        }
        if(!dbfile.exists()){
            val file = resources.openRawResource(R.raw.music_list)
            val fileSize = file.available()
            val buffer = ByteArray(fileSize)
            file.read(buffer)
            file.close()
            dbfile.createNewFile()
            val output = FileOutputStream(dbfile)
            output.write(buffer)
            output.close()
        }
    }


}




