package com.kuroutine.kulture.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.ChatModel
import com.example.domain.dto.DashboardQuestionModel
import com.example.kuroutine.R
import com.kuroutine.kulture.data.ChatViewType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrivateChatAdapter(
    private val viewModel: ChatViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var myUId: String = ""
    private val diffUtil = AsyncListDiffer(this, DiffUtils())

    class LeftViewHolder(
        view: View,
        private val viewModel: ChatViewModel
    ) : RecyclerView.ViewHolder(view) {
        private val tvMessage = view.findViewById<TextView>(R.id.tv_chat_message_left)
        private val tvTimeStamp = view.findViewById<TextView>(R.id.tv_chat_timestamp_left)

        suspend fun bind(data: ChatModel) {
            tvMessage.text = translate(data.message)
            tvTimeStamp.text = data.timestamp.toString()
        }

        suspend fun translate(target: String): String {
            val langCode = viewModel.checkLanguage(target)
            return if (langCode == viewModel.language.value) {
                ""
            } else {
                viewModel.getTranslatedText(target, langCode) ?: target
            }
        }
    }

    class RightViewHolder(
        view: View,
        private val viewModel: ChatViewModel
    ) : RecyclerView.ViewHolder(view) {
        private val tvMessage = view.findViewById<TextView>(R.id.tv_chat_message_right)
        private val tvTimeStamp = view.findViewById<TextView>(R.id.tv_chat_timestamp_right)

        suspend fun bind(data: ChatModel) {
            tvMessage.text = translate(data.message)
            tvTimeStamp.text = data.timestamp.toString()
        }

        suspend fun translate(target: String): String {
            val langCode = viewModel.checkLanguage(target)
            return if (langCode == viewModel.language.value) {
                ""
            } else {
                viewModel.getTranslatedText(target, langCode) ?: target
            }
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
                RightViewHolder(view, viewModel)
            }

            ChatViewType.LEFT.value -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_left, parent, false)
                LeftViewHolder(view, viewModel)
            }

            else -> throw RuntimeException("viewType not found.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = diffUtil.currentList[position]
        when (holder) {
            is LeftViewHolder -> {
                CoroutineScope(Dispatchers.Main).launch { holder.bind(data) }
            }

            is RightViewHolder -> {
                CoroutineScope(Dispatchers.Main).launch { holder.bind(data) }
            }

            else -> throw RuntimeException("viewType not found.")
        }
    }

    fun getUser(uid: String) {
        myUId = uid
    }

    fun submitList(nList: List<ChatModel>) {
        diffUtil.submitList(nList)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    class DiffUtils : DiffUtil.ItemCallback<ChatModel>() {
        override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem.message == newItem.message && oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem == newItem
        }

    }
}