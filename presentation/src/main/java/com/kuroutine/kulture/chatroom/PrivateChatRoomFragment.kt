package com.kuroutine.kulture.chatroom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuroutine.databinding.FragmentPrivateChatroomBinding
import com.kuroutine.kulture.EXTRA_KEY_ISPRIVATE
import com.kuroutine.kulture.EXTRA_KEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_QKEY_MOVETOCHAT
import com.kuroutine.kulture.chat.ChatActivity
import com.kuroutine.kulture.chat.ChatViewModel
import com.kuroutine.kulture.home.HomeListAdapter
import dagger.hilt.android.AndroidEntryPoint
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
        binding.rvPrivateChatroomList.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rvPrivateChatroomList.adapter = ChatRoomAdapter(moveToChatActivity = ::moveToChatActivity)
        }
    }

    private fun initObserver() {
        chatRoomViewModel.chatRoomModelList.observe(viewLifecycleOwner) {
            (binding.rvPrivateChatroomList.adapter as ChatRoomAdapter).submitList(it)
        }
    }

    private fun moveToChatActivity(qid: String, uid: String, isPrivate: Boolean) {
        val intent = Intent(this.context, ChatActivity::class.java)
        intent.putExtra(EXTRA_KEY_MOVETOCHAT, uid)
        intent.putExtra(EXTRA_QKEY_MOVETOCHAT, qid)
        intent.putExtra(EXTRA_KEY_ISPRIVATE, isPrivate)
        startActivity(intent)
    }

    fun getAdapter(): HomeListAdapter? {
        _binding?.let {
            it.rvPrivateChatroomList.adapter?.let {
                return binding.rvPrivateChatroomList.adapter as HomeListAdapter
            } ?: run {
                return null
            }
        } ?: run {
            return null
        }
    }
}