package com.kuroutine.kulture.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.ChatModel
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.usecase.chat.ChatUsecase
import com.example.domain.usecase.dashboard.DashboardUsecase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUsecase: ChatUsecase,
    private val dashboardUsecase: DashboardUsecase
) : ViewModel() {
    private val _currentUser = MutableLiveData<FirebaseUser?>().apply { value = null }
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _chatModelList = MutableLiveData<List<ChatModel>?>().apply { value = null }
    val chatModelList: LiveData<List<ChatModel>?> = _chatModelList

    private val _chat = MutableLiveData<DashboardQuestionModel?>().apply { value = null }
    val chat: LiveData<DashboardQuestionModel?> = _chat

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = chatUsecase.getCurrentUser()
        }
    }

    // 방에 맨 처음 입장할 때 실행.
    fun initChatRoom(qid: String, compUid: String, initialCallback: (() -> Unit)) {
        viewModelScope.launch {
            chatUsecase.initRoom(qid, compUid) {
                initialCallback()
            }
        }
    }

    fun getQuestion(qid: String) {
        viewModelScope.launch {
            _chat.value = dashboardUsecase.getQuestion(qid)
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            chatUsecase.sendMessage(message)
        }
    }

    fun getMessages(animationCallback: (() -> Unit)) {
        viewModelScope.launch {
            chatUsecase.getMessages { it ->
                _chatModelList.value = it
                animationCallback()
            }
        }
    }
}