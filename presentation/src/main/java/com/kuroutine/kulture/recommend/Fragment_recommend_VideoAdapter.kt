package com.kuroutine.kulture.recommend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class Fragment_recommend_VideoAdapter(var items: MutableList<YouTubePlayerView>) :
    RecyclerView.Adapter<Fragment_recommend_VideoAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var youtube_player_view: YouTubePlayerView

        init {
            youtube_player_view = itemView.findViewById(R.id.youtube_player_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommend, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.youtube_player_view = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}