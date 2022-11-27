package com.kuroutine.kulture.ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.example.domain.dto.ChatModel
import com.example.domain.dto.UserModel
import com.example.kuroutine.R
import com.kuroutine.kulture.chat.PrivateChatAdapter

class RankingViewAdapter: RecyclerView.Adapter<RankingViewAdapter.ViewHolder>() {

    private val diffUtil = AsyncListDiffer(this, RankingViewAdapter.DiffUtils())
    private val medalList = listOf(
        R.drawable.icon_medal_gold,
        R.drawable.icon_medal_silver,
        R.drawable.icon_medal_bronze
    )

    // recyclerview 재사용에서 생기는 오류 방지
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(diffUtil.currentList[position], position + 1)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val userPic = itemView.findViewById<ImageView>(R.id.iv_rank_thumbnail)
        private val userName = itemView.findViewById<TextView>(R.id.tv_rank_username)
        private val userLevel = itemView.findViewById<TextView>(R.id.tv_ranking_level)
        private val userRankText = itemView.findViewById<TextView>(R.id.tv_ranking_order)
        private val userRankImage = itemView.findViewById<ImageView>(R.id.iv_ranking_order_replace)

        fun bind(data: UserModel, index: Int) {
            Glide.with(itemView.context)
                .load(if (data.profile != "") data.profile else R.drawable.icon_profile)
                .circleCrop()
                .into(userPic)

            userName.text = data.userName
            userLevel.text = data.userRank

            if (index <= 3) {
                userRankText.visibility = View.GONE
                userRankImage.visibility = View.VISIBLE

                Glide.with(itemView.context)
                    .load(medalList[index - 1])
                    .into(userRankImage)
            } else {
                userRankText.visibility = View.VISIBLE
                userRankImage.visibility = View.GONE

                userRankText.text = index.toString()
            }
        }
    }

    fun submitList(nList: List<UserModel>) {
        diffUtil.submitList(nList)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    class DiffUtils : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem == newItem
        }
    }
}