package com.kuroutine.kulture.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.Chat
import com.example.domain.usecase.chat.ChatUsecase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUsecase: ChatUsecase
) : ViewModel() {
    private val _currentUser = MutableLiveData<FirebaseUser?>().apply { value = null }
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _chatList = MutableLiveData<List<Chat>?>().apply { value = null }
    val chatList: LiveData<List<Chat>?> = _chatList

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = chatUsecase.getCurrentUser()
        }
    }

    // 방에 맨 처음 입장할 때 실행.
    fun initChatRoom(compUid: String, initialCallback: (() -> Unit)) {
        viewModelScope.launch {
            chatUsecase.initRoom(compUid) {
                initialCallback()
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            chatUsecase.sendMessage(message)
        }
    }

    fun getMessages(animationCallback: (() -> Unit)) {
        viewModelScope.launch {
            chatUsecase.getMessages {
                _chatList.value = it
                animationCallback()
                Log.d("[keykat]", "list: ${_chatList.value.toString()}")
            }
        }
    }
}