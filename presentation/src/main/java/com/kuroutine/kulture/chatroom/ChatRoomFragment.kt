package com.kuroutine.kulture.chatroom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroutine.databinding.FragmentChatroomBinding
import com.kuroutine.kulture.EXTRA_KEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_QKEY_MOVETOCHAT
import com.kuroutine.kulture.chat.ChatActivity
import com.kuroutine.kulture.chat.PrivateChatAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    private lateinit var chatRoomViewModel: ChatRoomViewModel
    private var _binding: FragmentChatroomBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatRoomViewModel =
            ViewModelProvider(this).get(ChatRoomViewModel::class.java)

        _binding = FragmentChatroomBinding.inflate(inflater, container, false).apply {
            viewModel = chatRoomViewModel
            lifecycleOwner = this@ChatRoomFragment
        }
        val root: View = binding.root


        return root
    }

    override fun onStart() {
        super.onStart()
        init()
        initObserver()
    }

    private fun init() {
        chatRoomViewModel.getChatRooms()
        binding.rvChatroomList.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rvChatroomList.adapter = ChatRoomAdapter(moveToChatActivity = ::moveToChatActivity)
        }
    }

    private fun initObserver() {
        chatRoomViewModel.chatRoomModelList.observe(this) {
            (binding.rvChatroomList.adapter as ChatRoomAdapter).submitList(it)
        }
    }

    private fun moveToChatActivity(qid: String, uid: String) {
        val intent = Intent(this.context, ChatActivity::class.java)
        intent.putExtra(EXTRA_KEY_MOVETOCHAT, uid)
        intent.putExtra(EXTRA_QKEY_MOVETOCHAT, qid)
        startActivity(intent)
    }
}