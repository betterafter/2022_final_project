package com.kuroutine.kulture.chat

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityPrivateChatBinding
import com.kuroutine.kulture.EXTRA_KEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_QKEY_MOVETOCHAT
import dagger.hilt.android.AndroidEntryPoint

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

        initAdapter()
        initObserver()

        initListener()
    }

    override fun onStart() {
        super.onStart()
        val qid = intent.getStringExtra(EXTRA_QKEY_MOVETOCHAT)
        val uid = intent.getStringExtra(EXTRA_KEY_MOVETOCHAT)
        init(qid, uid)
    }

    private fun init(qid: String?, uid: String?) {
        if (qid != null && uid != null) {
            Log.d("[keykat]","uid: $uid")
            chatViewModel.initChatRoom(qid, uid) {
                chatViewModel.getMessages {
                    chatViewModel.chatModelList.value?.let {
                        if (it.isNotEmpty()) {
                            binding.rvPrivatechatChatrv.smoothScrollToPosition(it.size - 1)
                        }
                    }
                }
            }
        }

        chatViewModel.getCurrentUser()
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
            binding.rvPrivatechatChatrv.adapter = PrivateChatAdapter()
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
    }
}