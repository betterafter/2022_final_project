package com.kuroutine.kulture.chatroom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.ChatModel
import com.example.domain.dto.ChatRoomModel
import com.example.domain.usecase.chat.ChatUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatUsecase: ChatUsecase
) : ViewModel() {
    private val _chatRoomModelList = MutableLiveData<List<ChatRoomModel>?>().apply { value = null }
    val chatRoomModelList: LiveData<List<ChatRoomModel>?> = _chatRoomModelList

    fun getChatRooms() {
        viewModelScope.launch {
            chatUsecase.getChatRooms {
                _chatRoomModelList.value = it
            }
        }
    }
}