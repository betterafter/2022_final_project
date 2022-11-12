package com.kuroutine.kulture.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.ChatModel
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.dto.UserModel
import com.example.domain.usecase.chat.ChatUsecase
import com.example.domain.usecase.dashboard.DashboardUsecase
import com.example.domain.usecase.papago.PapagoUsecase
import com.example.domain.usecase.user.UserUsecase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUsecase: ChatUsecase,
    private val dashboardUsecase: DashboardUsecase,
    private val papagoUsecase: PapagoUsecase,
    private val userUsecase: UserUsecase
) : ViewModel() {
    private val _language = MutableLiveData<String?>().apply { value = null }
    val language: LiveData<String?> = _language

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

    fun clearList() {
        _chatModelList.value = null
    }

    // 방에 맨 처음 입장할 때 실행.
    fun initChatRoom(qid: String, compUid: String?, isPrivate: Boolean, initialCallback: (() -> Unit)) {
        viewModelScope.launch {
            chatUsecase.initRoom(qid, compUid, isPrivate) {
                initialCallback()
            }
        }
    }

    suspend fun getUser(uid: String): UserModel? {
        return userUsecase.getUser(uid)
    }

    suspend fun getLanguage() {
        userUsecase.getUser(null) {
            if (_language.value != it.language) {
                _language.value = it.language
            }
        }
    }

    suspend fun checkLanguage(data: String): String {
        return papagoUsecase.getLangCode(data) ?: "ko"
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

    suspend fun getTranslatedText(data: String, code: String): String? {
        return papagoUsecase.getText(data, code, _language.value ?: "ko")
    }
}