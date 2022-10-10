package com.kuroutine.kulture.chat

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityPrivateChatBinding
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

        initListener()
        initAdapter()
        initObserver()

        init()
    }

    private fun init() {
        chatViewModel.initChatRoom("8xjNJvtgpwfo2oiC2Di9nx4Wyrk1")
        chatViewModel.getMessages()
        chatViewModel.getCurrentUser()
    }

    private fun initListener() {
        binding.ivPrivatechatSendbutton.setOnClickListener {
            chatViewModel.sendMessage(binding.etPrivatechatMessagebox.text.toString())
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
                chatViewModel.chatList.value?.let {
                    adapter.submitList(it)
                } ?: run {
                    adapter.submitList(listOf())
                }
            }
        }

        chatViewModel.chatList.observe(this) {
            it?.forEach {
                Log.d("[keykat]", "${it.message}")
            }
            it?.let { list ->
                (binding.rvPrivatechatChatrv.adapter as PrivateChatAdapter).submitList(list)
            }
        }
    }
}