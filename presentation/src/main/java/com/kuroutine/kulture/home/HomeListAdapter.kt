package com.kuroutine.kulture.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.dto.DashboardQuestionModel
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ItemHomeBinding

class HomeListAdapter(
    val moveToChatActivity: (String, String) -> Unit
) : ListAdapter<DashboardQuestionModel, HomeListAdapter.ViewHolder>(DiffUtils()) {

    class ViewHolder(
        private val binding: ItemHomeBinding,
        private val callback: (String, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DashboardQuestionModel) {
            binding.tvHomeUserid.text = data.userName
            binding.tvHomeQuestion.text = data.title
            Glide.with(binding.root.context).load(data.imageList?.first())
                .override(500, 500)
                .into(binding.ivHomeThumbnail)
            // TODO: 나머지도 데이터 연결할 것

            binding.cvHomeItem.setOnClickListener {
                callback(data.id, data.uid)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            moveToChatActivity
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class DiffUtils : DiffUtil.ItemCallback<DashboardQuestionModel>() {
        override fun areItemsTheSame(oldItem: DashboardQuestionModel, newItem: DashboardQuestionModel): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: DashboardQuestionModel, newItem: DashboardQuestionModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = currentList[position]
        holder.bind(data)
    }
}