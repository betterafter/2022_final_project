package com.kuroutine.kulture.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.domain.entity.DashboardQuestionModel
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ActivityPrivateChatBinding
import com.kuroutine.kulture.DetailDialog
import com.kuroutine.kulture.EXTRA_KEY_ISPRIVATE
import com.kuroutine.kulture.EXTRA_KEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_KEY_USERS
import com.kuroutine.kulture.EXTRA_MAIN_VIEWPAGER_INDEX
import com.kuroutine.kulture.EXTRA_QKEY_MOVETOCHAT
import com.kuroutine.kulture.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivateChatBinding
    private val chatViewModel by viewModels<ChatViewModel>()

    var qid: String? = null
    var uid: String? = null
    var users: Array<String>? = null
    var isPrivate: String? = null

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

        val bundle = intent.extras

//        Log.d("[keykat]", "qid1::: ${bundle?.getString("qid")}")
//        Log.d("[keykat]", "qid2::: ${intent.getStringExtra(
//            EXTRA_QKEY_MOVETOCHAT
//        )}")
//
//        Log.d("[keykat]", "uid1::: ${bundle?.getString("uid")}")
//        Log.d("[keykat]", "uid2::: ${intent.getStringExtra(
//            EXTRA_KEY_MOVETOCHAT
//        )}")
//
//        Log.d("[keykat]", "isPrivate1::: ${bundle?.getString("isPrivate")}")
//        Log.d("[keykat]", "isPrivate2::: ${intent.getBooleanExtra(
//            EXTRA_KEY_ISPRIVATE
//        , true)}")
//
//        Log.d("[keykat]", "users1::: ${bundle?.getString("users")}")
//        Log.d("[keykat]", "users2::: ${intent.getStringArrayExtra(
//            EXTRA_KEY_USERS
//        )}")

        qid = if (bundle?.getString("qid") != null) bundle.getString("qid") else intent.getStringExtra(
            EXTRA_QKEY_MOVETOCHAT
        )
        uid = if (bundle?.getString("uid") != null) bundle.getString("uid") else intent.getStringExtra(
            EXTRA_KEY_MOVETOCHAT
        )
        users = if (bundle?.getString("users") != null) {
            val usersString = bundle.getString("users")
            usersString?.split(",")?.toTypedArray()
        } else {
            intent.getStringArrayExtra(EXTRA_KEY_USERS)
        }

        isPrivate =
            if (bundle?.getString("isPrivate") != null) bundle.getString("isPrivate") else intent.getBooleanExtra(
                EXTRA_KEY_ISPRIVATE, true
            ).toString()

        // Log.d("[keykat]", "qid: $qid, uid: $uid, users: $users, isprivate: $isPrivate")
        init(qid, uid, isPrivate)
        initAdapter()
        initListener()
        initObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        intent.extras?.clear()
        this.finish()
    }

    private fun init(qid: String?, uid: String?, isPrivate: String?) {
        if (qid != null) {
            val private = isPrivate == "true"
            chatViewModel.initChatRoom(qid, uid, isPrivate = private) { room ->
                CoroutineScope(Dispatchers.Main).launch {
                    room?.let {
                        if (it.end) {
                            binding.etPrivatechatMessagebox.setText("종료된 대화방입니다.")
                            binding.etPrivatechatMessagebox.isEnabled = false
                        } else {
                            binding.etPrivatechatMessagebox.isEnabled = true
                        }
                    }
                }

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
            val message = binding.etPrivatechatMessagebox.text.toString()
            binding.etPrivatechatMessagebox.text.clear()

            users?.forEach {
                CoroutineScope(Dispatchers.IO).launch {
                    val currUser = chatViewModel.currentUser.value?.uid?.let { it1 -> chatViewModel.getUser(it1) }
                    if (chatViewModel.currentUser.value?.uid != it) {
                        val user = chatViewModel.getUser(it)
                        chatViewModel.sendPushMessage(
                            to = user?.messageToken ?: "",
                            title = currUser?.userName ?: "unknown",
                            body = message,
                            qid = qid ?: "",
                            uid = uid ?: "",
                            users = users?.toList() ?: listOf(),
                            userProfile = currUser?.profile ?: "",
                            isPrivate = isPrivate == "true"
                        )
                    }
                }
            }
        }

        binding.ivPrivatechatBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivPrivatechatFinish.setOnClickListener {
            val dialog = DetailDialog(
                "채팅을 종료합니다",
                "답변자가 도움이 되셨나요? (질문자 및 답변자 내공 +10)",
                {
                    qid?.let { it1 -> chatViewModel.updateQuestionFinishState(it1, true) }
                    users?.forEach { uid ->
                        chatViewModel.updateUserXp(uid, 10)
                    }
                    this.finish()
                }, {

                }
            )
            dialog.show(supportFragmentManager, "")
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
                Log.d("[keykat]", "chats: $it")
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

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(EXTRA_MAIN_VIEWPAGER_INDEX, R.id.navigation_chat)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}