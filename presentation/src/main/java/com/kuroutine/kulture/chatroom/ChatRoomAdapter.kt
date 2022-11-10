package com.kuroutine.kulture.chatroom

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.ChatRoomModel
import com.example.kuroutine.databinding.ItemChatroomBinding

class ChatRoomAdapter(
    val moveToChatActivity: (String, String, Boolean) -> Unit
) : ListAdapter<ChatRoomModel, ChatRoomAdapter.ViewHolder>(DiffUtils()) {

    class ViewHolder(
        private val binding: ItemChatroomBinding,
        private val callback: (String, String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ChatRoomModel) {
            binding.tvItemChatroomContent.text = data.contents?.last()?.message
            binding.cvChatroomItem.setOnClickListener {
                data.users?.forEach {
                    if (it.value) {
                        callback(data.qid, it.key, data.isPrivate)
                        return@forEach
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatroomBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            moveToChatActivity
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class DiffUtils : DiffUtil.ItemCallback<ChatRoomModel>() {
        override fun areItemsTheSame(oldItem: ChatRoomModel, newItem: ChatRoomModel): Boolean {
            return oldItem.contents == newItem.contents
        }

        override fun areContentsTheSame(oldItem: ChatRoomModel, newItem: ChatRoomModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = currentList[position]
        holder.bind(data)
    }
}