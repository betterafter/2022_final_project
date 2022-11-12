package com.kuroutine.kulture.chatroom

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.domain.dto.ChatRoomModel
import com.example.kuroutine.databinding.ItemChatroomBinding
import com.kuroutine.kulture.chat.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatRoomAdapter(
    val moveToChatActivity: (String, String, Boolean) -> Unit,
    private val viewModel: ChatRoomViewModel
) : ListAdapter<ChatRoomModel, ChatRoomAdapter.ViewHolder>(DiffUtils()) {

    class ViewHolder(
        private val binding: ItemChatroomBinding,
        private val callback: (String, String, Boolean) -> Unit,
        private val viewModel: ChatRoomViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        suspend fun bind(data: ChatRoomModel) {
            data.contents?.last()?.message?.let {
                binding.tvItemChatroomContent.text = translate(it)
            }
            binding.cvChatroomItem.setOnClickListener {
                data.users?.forEach {
                    if (it.value) {
                        callback(data.qid, it.key, data.isPrivate)
                        return@forEach
                    }
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                val question = viewModel.getQuestion(data.qid)
                question?.let {
                    Glide.with(binding.root.context)
                        .load(it.imageList?.first())
                        .transform(RoundedCorners(15))
                        .into(binding.ivChatroomQuestionImage)

                    binding.tvChatroomQuestionTitle.text = translate(it.title)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatroomBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            moveToChatActivity,
            viewModel
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
        CoroutineScope(Dispatchers.Main).launch {
            holder.bind(data)
        }
    }
}