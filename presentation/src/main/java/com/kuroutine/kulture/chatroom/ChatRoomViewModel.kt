package com.kuroutine.kulture.chatroom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.ChatModel
import com.example.domain.dto.ChatRoomModel
import com.example.domain.usecase.chat.ChatUsecase
import com.example.domain.usecase.papago.PapagoUsecase
import com.example.domain.usecase.user.UserUsecase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatUsecase: ChatUsecase,
    private val papagoUsecase: PapagoUsecase,
    private val userUsecase: UserUsecase
) : ViewModel() {
    private val _language = MutableLiveData<String?>().apply { value = null }
    val language: LiveData<String?> = _language

    private val _currentUser = MutableLiveData<FirebaseUser?>().apply { value = null }
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _chatRoomModelList = MutableLiveData<List<ChatRoomModel>?>().apply { value = null }
    val chatRoomModelList: LiveData<List<ChatRoomModel>?> = _chatRoomModelList

    private val _publicChatRoomModelList = MutableLiveData<List<ChatRoomModel>?>().apply { value = null }
    val publicChatRoomModelList: LiveData<List<ChatRoomModel>?> = _publicChatRoomModelList

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = chatUsecase.getCurrentUser()
        }
    }

    fun getChatRooms() {
        viewModelScope.launch {
            chatUsecase.getChatRooms { list1, list2 ->
                _chatRoomModelList.value = list1
                _publicChatRoomModelList.value = list2
            }
        }
    }

    suspend fun checkLanguage(data: String): String {
        return papagoUsecase.getLangCode(data) ?: "ko"
    }

    suspend fun getLanguage() {
        userUsecase.getUser(null) {
            if (_language.value != it.language) {
                _language.value = it.language
            }
        }
    }

    suspend fun getTranslatedText(data: String, code: String): String? {
        return papagoUsecase.getText(data, code, _language.value ?: "ko")
    }
}