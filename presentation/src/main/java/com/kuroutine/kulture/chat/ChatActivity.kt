package com.kuroutine.kulture.chat

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.domain.dto.DashboardQuestionModel
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityPrivateChatBinding
import com.kuroutine.kulture.EXTRA_KEY_ISPRIVATE
import com.kuroutine.kulture.EXTRA_KEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_QKEY_MOVETOCHAT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivateChatBinding
    private val chatViewModel by viewModels<ChatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView<ActivityPrivateChatBinding?>(this, R.layout.activity_private_chat).apply {
                viewModel = chatViewModel
                lifecycleOwner = this@ChatActivity
            }
    }

    override fun onStart() {
        super.onStart()
        val qid = intent.getStringExtra(EXTRA_QKEY_MOVETOCHAT)
        val uid = intent.getStringExtra(EXTRA_KEY_MOVETOCHAT)
        val isPrivate = intent.getBooleanExtra(EXTRA_KEY_ISPRIVATE, false)
        init(qid, uid, isPrivate)
        initAdapter()
        initObserver()
        initListener()
    }

    private fun init(qid: String?, uid: String?, isPrivate: Boolean) {
        if (qid != null) {
            chatViewModel.initChatRoom(qid, uid, isPrivate = isPrivate) {
                chatViewModel.getMessages {
                    chatViewModel.chatModelList.value?.let {
                        if (it.isNotEmpty()) {
                            binding.rvPrivatechatChatrv.smoothScrollToPosition(it.size)
                        }
                    }
                }
            }
        }

        qid?.let { chatViewModel.getQuestion(qid) }
        chatViewModel.getCurrentUser()

        CoroutineScope(Dispatchers.Main).launch {
            chatViewModel.getLanguage()
        }
    }

    private fun initListener() {
        binding.ivPrivatechatSendbutton.setOnClickListener {
            chatViewModel.sendMessage(binding.etPrivatechatMessagebox.text.toString())
            binding.etPrivatechatMessagebox.text.clear()
        }
    }

    private fun initAdapter() {
        binding.rvPrivatechatChatrv.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rvPrivatechatChatrv.adapter = PrivateChatAdapter(chatViewModel)
        }
    }

    private fun initObserver() {
        chatViewModel.currentUser.observe(this) { user ->
            user?.let {
                val adapter = binding.rvPrivatechatChatrv.adapter as PrivateChatAdapter
                adapter.getUser(user.uid)
                chatViewModel.chatModelList.value?.let {
                    adapter.submitList(it)
                } ?: run {
                    adapter.submitList(listOf())
                }
            }
        }

        chatViewModel.chatModelList.observe(this) {
            it?.let { list ->
                (binding.rvPrivatechatChatrv.adapter as PrivateChatAdapter).submitList(list)
            }
        }

        chatViewModel.chat.observe(this) {
            it?.let {
                it.imageList?.first()?.let { url ->
                    Glide.with(binding.root.context)
                        .load(url)
                        .transform(RoundedCorners(15))
                        .into(binding.ivPrivatechatPhoto)
                } ?: run {
                    Glide.with(binding.root.context).load(R.drawable.ic_baseline_insert_photo_24)
                        //.transform(GranularRoundedCorners(30F, 0F, 0F, 30F))
                        .circleCrop()
                        .into(binding.ivPrivatechatPhoto)
                }

                CoroutineScope(Dispatchers.Main).launch {
                    it.translatedTitle = translate(it.title, it)
                    it.translatedText = translate(it.text, it)
                    binding.tvPrivatechatTitle.text = it.translatedTitle
                    binding.tvPrivatechatDetails.text = it.translatedText
                }
            }
        }
    }

    suspend fun translate(target: String, data: DashboardQuestionModel): String {
        data.translatedState = true
        val langCode = chatViewModel.checkLanguage(target)
        return if (langCode == chatViewModel.language.value) {
            ""
        } else {
            chatViewModel.getTranslatedText(target, langCode) ?: target
        }
    }
}