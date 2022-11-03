package com.kuroutine.kulture.ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.example.kuroutine.R

class RankingViewAdapter(
    private val values: ArrayList<RankingData>
) : RecyclerView.Adapter<RankingViewAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:RankingData, position: Int) //position 정보 추가
    }

    var itemClickListener:OnItemClickListener?=null

    // recyclerview 재사용에서 생기는 오류 방지
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.user_pic.setImageResource(values[position].picture)
        holder.user_name.text = values[position].name
        holder.user_level.text = values[position].level.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val user_pic = itemView.findViewById<ImageView>(R.id.imageView3)
        val user_name = itemView.findViewById<TextView>(R.id.textView)
        val user_level = itemView.findViewById<TextView>(R.id.textView2)

    }

}