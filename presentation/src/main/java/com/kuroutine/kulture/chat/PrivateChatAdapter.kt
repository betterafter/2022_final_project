package com.kuroutine.kulture.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.dto.ChatModel
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
        private val view: View,
        private val viewModel: ChatViewModel
    ) : RecyclerView.ViewHolder(view) {
        private val tvMessage = view.findViewById<TextView>(R.id.tv_chat_message_left)
        private val tvTimeStamp = view.findViewById<TextView>(R.id.tv_chat_timestamp_left)
        private val ivUserProfile = view.findViewById<ImageView>(R.id.iv_chat_item_left)
        private val tvUserName = view.findViewById<TextView>(R.id.tv_chat_item_left_user_name)


        suspend fun bind(data: ChatModel, prevData: ChatModel?, nextData: ChatModel?) {
            tvMessage.text = translate(data.message)
            tvTimeStamp.text = data.timestamp.toString()
            data.translatedMessage = tvMessage.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                val user = viewModel.getUser(data.uid)
                user?.let { it ->
                    if (prevData == null || prevData.uid != data.uid) {
                        Glide.with(view.context)
                            .load(if (user.profile != "") it.profile else R.drawable.icon_profile)
                            .circleCrop()
                            .into(ivUserProfile)
                        ivUserProfile.visibility = View.VISIBLE

                        tvUserName.visibility = View.VISIBLE
                        tvUserName.text = it.userName

                        if (nextData?.timestamp == data.timestamp) {
                            tvTimeStamp.visibility = View.GONE
                        } else {
                            tvTimeStamp.visibility = View.VISIBLE
                        }
                    } else {
                        tvUserName.visibility = View.GONE
                        ivUserProfile.visibility = View.INVISIBLE
                        return@launch
                    }
                }
            }
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

        suspend fun bind(data: ChatModel, prevData: ChatModel?, nextData: ChatModel?) {
            tvMessage.text = translate(data.message)
            tvTimeStamp.text = data.timestamp.toString()

            if (nextData?.timestamp == data.timestamp) {
                tvTimeStamp.visibility = View.GONE
            } else {
                tvTimeStamp.visibility = View.VISIBLE
            }
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
        val prevData: ChatModel? = if (position - 1 >= 0) {
            diffUtil.currentList[position - 1]
        } else {
            null
        }
        val nextData: ChatModel? = if (diffUtil.currentList.size <= position + 1) {
            null
        } else {
            diffUtil.currentList[position + 1]
        }

        when (holder) {
            is LeftViewHolder -> {
                CoroutineScope(Dispatchers.Main).launch { holder.bind(data, prevData, nextData) }
            }

            is RightViewHolder -> {
                CoroutineScope(Dispatchers.Main).launch { holder.bind(data, prevData, nextData) }
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