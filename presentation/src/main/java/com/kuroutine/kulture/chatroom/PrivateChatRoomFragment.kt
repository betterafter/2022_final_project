package com.kuroutine.kulture.chatroom

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroutine.databinding.FragmentPrivateChatroomBinding
import com.kuroutine.kulture.EXTRA_KEY_ISPRIVATE
import com.kuroutine.kulture.EXTRA_KEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_KEY_USERS
import com.kuroutine.kulture.EXTRA_QKEY_MOVETOCHAT
import com.kuroutine.kulture.chat.ChatActivity
import com.kuroutine.kulture.chat.ChatViewModel
import com.kuroutine.kulture.home.HomeListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrivateChatRoomFragment : Fragment() {

    private val chatRoomViewModel: ChatRoomViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private var _binding: FragmentPrivateChatroomBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrivateChatroomBinding.inflate(inflater, container, false).apply {
            viewModel = chatRoomViewModel
            lifecycleOwner = this@PrivateChatRoomFragment
        }
        val root: View = binding.root

        init()
        initObserver()

        return root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun init() {
        Log.d("[keykat]", "private init")

        binding.rvPrivateChatroomList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvPrivateChatroomList.adapter =
                ChatRoomAdapter(moveToChatActivity = ::moveToChatActivity, chatRoomViewModel)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        chatRoomViewModel.chatRoomModelList.observe(viewLifecycleOwner) {
            (binding.rvPrivateChatroomList.adapter as ChatRoomAdapter).submitList(it)
            (binding.rvPrivateChatroomList.adapter as ChatRoomAdapter).notifyDataSetChanged()
        }
    }

    private fun moveToChatActivity(qid: String, uid: String, users: Array<String>, isPrivate: Boolean) {
        val intent = Intent(this.context, ChatActivity::class.java)
        intent.putExtra(EXTRA_KEY_MOVETOCHAT, uid)
        intent.putExtra(EXTRA_QKEY_MOVETOCHAT, qid)
        intent.putExtra(EXTRA_KEY_USERS, users)
        intent.putExtra(EXTRA_KEY_ISPRIVATE, isPrivate)
        startActivity(intent)
    }

    fun getAdapter(): ChatRoomAdapter? {
        _binding?.let {
            it.rvPrivateChatroomList.adapter?.let {
                return binding.rvPrivateChatroomList.adapter as ChatRoomAdapter
            } ?: run {
                Log.d("[keykat]", "private adapter null")
                return null
            }
        } ?: run {
            Log.d("[keykat]", "private adapter null")
            return null
        }
    }
}