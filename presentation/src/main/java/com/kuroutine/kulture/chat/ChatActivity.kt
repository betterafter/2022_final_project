package com.kuroutine.kulture.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
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
import com.kuroutine.kulture.data.ChatViewType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_private_chat.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivateChatBinding
    private val chatViewModel by viewModels<ChatViewModel>()

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView<ActivityPrivateChatBinding?>(this, R.layout.activity_private_chat).apply {
                viewModel = chatViewModel
                lifecycleOwner = this@ChatActivity
            }


        tts = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.KOREAN
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        val qid = intent.getStringExtra(EXTRA_QKEY_MOVETOCHAT)
        val uid = intent.getStringExtra(EXTRA_KEY_MOVETOCHAT)
        val isPrivate = intent.getBooleanExtra(EXTRA_KEY_ISPRIVATE, false)

        init(qid, uid, isPrivate)
        initAdapter()
        initListener()
        initObserver()
    }

    private fun init(qid: String?, uid: String?, isPrivate: Boolean) {
        if (qid != null) {
            chatViewModel.initChatRoom(qid, uid, isPrivate = isPrivate) {
                CoroutineScope(Dispatchers.Main).launch {
                    chatViewModel.getLanguage()
                }
            }
        }

        qid?.let { chatViewModel.getQuestion(qid) }
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
            binding.rvPrivatechatChatrv.adapter =
                PrivateChatAdapter(chatViewModel, tts, notifyCallback = ::notifyCallback)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        chatViewModel.currentUser.observe(this) { user ->
            user?.let {
                val adapter = binding.rvPrivatechatChatrv.adapter as PrivateChatAdapter
                adapter.getUser(user.uid)
            }
        }

        chatViewModel.language.observe(this) {
            chatViewModel.getMessages {
                chatViewModel.chatModelList.value?.let {
                    if (it.isNotEmpty()) {
                        binding.rvPrivatechatChatrv.smoothScrollToPosition(it.size)
                    }
                }
            }
        }

        chatViewModel.chatModelList.observe(this) {
            it?.let { list ->
                (binding.rvPrivatechatChatrv.adapter as PrivateChatAdapter).submitList(list)
                (binding.rvPrivatechatChatrv.adapter as PrivateChatAdapter).notifyDataSetChanged()
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
                    translate(it.title, it) { text -> it.translatedTitle = text }
                    translate(it.text, it) { text -> it.translatedText = text }
                    binding.tvPrivatechatTitle.text = it.translatedTitle
                    binding.tvPrivatechatDetails.text = it.translatedText
                }
            }
        }
    }

    suspend fun translate(target: String, data: DashboardQuestionModel, callback: (String) -> Unit) {
        data.translatedState = true
        chatViewModel.checkLanguage(target) {
            CoroutineScope(Dispatchers.Main).launch {
                val langCode = it
                chatViewModel.getTranslatedText(target, null, null) { text ->
                    callback(text)
                }
            }
        }
    }

    fun notifyCallback(index: Int) {
        //(binding.rvPrivatechatChatrv.adapter as PrivateChatAdapter).notifyItemChanged(index)
    }
}