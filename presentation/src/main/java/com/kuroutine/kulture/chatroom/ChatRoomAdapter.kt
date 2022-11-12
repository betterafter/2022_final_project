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
import com.example.kuroutine.R
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

            // 채팅방 대화창 및 프로필 디자인
            data.contents?.last()?.let { model ->
                binding.tvItemChatroomContent.text = translate(model.message)
                binding.tvItemChatroomTime.text = model.timestamp.toString()

                // 유저 프로필 이미지 및 이름 가져오기
                val user = viewModel.getUser(model.uid)
                user?.let {
                    Glide.with(binding.root.context)
                        .load(if (it.profile != "") it.profile else R.drawable.icon_profile)
                        .circleCrop()
                        .into(binding.ivItemChatroomImage)

                    binding.tvItemChatroomName.text = it.userName
                } ?: run {
                    Glide.with(binding.root.context)
                        .load(R.drawable.icon_profile)
                        .circleCrop()
                        .into(binding.ivItemChatroomImage)

                    binding.tvItemChatroomName.text = "unknown"
                }
            }

            // 채팅방 눌렀을 때 대화방으로 이동
            binding.cvChatroomItem.setOnClickListener {
                data.users?.forEach {
                    if (it.value) {
                        callback(data.qid, it.key, data.isPrivate)
                        return@forEach
                    }
                }
            }

            // 채팅방 상단 정보 디자인
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