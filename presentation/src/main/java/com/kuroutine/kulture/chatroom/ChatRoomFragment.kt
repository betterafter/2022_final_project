package com.kuroutine.kulture.chatroom

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kuroutine.databinding.FragmentChatroomBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kuroutine.kulture.home.HomeListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    private lateinit var chatRoomViewModel: ChatRoomViewModel
    private var _binding: FragmentChatroomBinding? = null

    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var privateChatRoomFragment: PrivateChatRoomFragment
    private lateinit var publicChatRoomFragment: PublicChatRoomFragment

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
        chatRoomViewModel =
            ViewModelProvider(this).get(ChatRoomViewModel::class.java)

        _binding = FragmentChatroomBinding.inflate(inflater, container, false).apply {
            viewModel = chatRoomViewModel
            lifecycleOwner = this@ChatRoomFragment
        }
        val root: View = binding.root

        privateChatRoomFragment = PrivateChatRoomFragment()
        publicChatRoomFragment = PublicChatRoomFragment()

        initFragment()
        init()
        initObserver()

        return root
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.Main).launch {
            chatRoomViewModel.getLanguage()
        }
    }

    private fun init() {
        binding.vpChatroomScreen.apply {
            adapter = ChatPager2Adapter(this@ChatRoomFragment, fragments)
        }

        TabLayoutMediator(binding.tlChatroomType, binding.vpChatroomScreen) { tab, position ->
            if (position == 0) {
                tab.text = "1:1 ?????? ?????????"
            } else {
                tab.text = "?????? ?????? ?????????"
            }
        }.attach()

        chatRoomViewModel.getCurrentUser()
    }

    private fun initFragment() {
        fragments = ArrayList()
        fragments.add(privateChatRoomFragment)
        fragments.add(publicChatRoomFragment)
    }

    private fun initObserver() {
        chatRoomViewModel.language.observe(viewLifecycleOwner) {
            chatRoomViewModel.getChatRooms()
        }
    }
}