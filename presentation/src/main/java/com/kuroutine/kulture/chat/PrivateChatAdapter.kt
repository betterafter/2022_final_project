package com.kuroutine.kulture.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.Chat
import com.example.kuroutine.R
import com.kuroutine.kulture.data.ChatViewType

class PrivateChatAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var myUId: String = ""
    private val diffUtil = AsyncListDiffer(this, DiffUtils())

    class LeftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvMessage = view.findViewById<TextView>(R.id.tv_chat_message_left)
        private val tvTimeStamp = view.findViewById<TextView>(R.id.tv_chat_timestamp_left)

        fun bind(data: Chat) {
            tvMessage.text = data.message
            tvTimeStamp.text = data.timestamp.toString()
        }
    }

    class RightViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvMessage = view.findViewById<TextView>(R.id.tv_chat_message_right)
        private val tvTimeStamp = view.findViewById<TextView>(R.id.tv_chat_timestamp_right)

        fun bind(data: Chat) {
            tvMessage.text = data.message
            tvTimeStamp.text = data.timestamp.toString()
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (diffUtil.currentList[position].uid == myUId) {
            ChatViewType.RIGHT.value
        } else {
            ChatViewType.LEFT.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            ChatViewType.RIGHT.value -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_right, parent, false)
                RightViewHolder(view)
            }

            ChatViewType.LEFT.value -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_left, parent, false)
                LeftViewHolder(view)
            }

            else -> throw RuntimeException("viewType not found.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = diffUtil.currentList[position]
        when (holder) {
            is LeftViewHolder -> {
                holder.bind(data)
            }

            is RightViewHolder -> {
                holder.bind(data)
            }

            else -> throw RuntimeException("viewType not found.")
        }
    }

    fun getUser(uid: String) {
        myUId = uid
    }

    fun submitList(nList: List<Chat>) {
        diffUtil.submitList(nList)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    class DiffUtils: DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.message == newItem.message && oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }

    }
}