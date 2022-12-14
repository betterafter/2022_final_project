package com.kuroutine.kulture.chat

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.ChatModel
import com.example.domain.dto.ChatRoomModel
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.dto.UserModel
import com.example.domain.usecase.chat.ChatUsecase
import com.example.domain.usecase.dashboard.DashboardUsecase
import com.example.domain.usecase.papago.TranslateUsecase
import com.example.domain.usecase.user.UserUsecase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUsecase: ChatUsecase,
    private val dashboardUsecase: DashboardUsecase,
    private val translateUsecase: TranslateUsecase,
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
    fun initChatRoom(qid: String, compUid: String?, isPrivate: Boolean, initialCallback: ((ChatRoomModel?) -> Unit)) {
        viewModelScope.launch {
            chatUsecase.initRoom(qid, compUid, isPrivate) {
                initialCallback(it)
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

    suspend fun checkLanguage(data: String, callback: (String) -> Unit) {
        translateUsecase.getLangCode(data) {
            callback(it)
        }
    }

    fun getQuestion(qid: String) {
        viewModelScope.launch {
            _chat.value = dashboardUsecase.getQuestion(qid)
        }
    }

    fun updateQuestionFinishState(qid: String, finish: Boolean) {
        viewModelScope.launch {
            chatUsecase.updateChat(qid, null, finish)
        }
    }

    fun updateUserXp(uid: String, xp: Int) {
        viewModelScope.launch {
            userUsecase.updateXp(uid, xp)
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            chatUsecase.sendMessage(message)
        }
    }

    fun sendPushMessage(
        to: String,
        title: String,
        body: String,
        qid: String,
        uid: String,
        users: List<String>,
        userProfile: String,
        isPrivate: Boolean
    ) {
        viewModelScope.launch {
            chatUsecase.sendPushMessage(
                to, title, body, qid, uid, users, userProfile, isPrivate
            )
        }
    }

    fun getMessages(animationCallback: (() -> Unit)) {
        clearList()

        viewModelScope.launch {
            val list = chatModelList.value

            chatUsecase.getMessages({ chatList ->
                _chatModelList.value = chatList
                animationCallback()

                viewModelScope.launch {
                    chatList.forEach { model ->
                        viewModelScope.launch {
                            checkLanguage(model.message) { lang ->
                                viewModelScope.launch {
                                    _language.value?.let { ulang ->
                                        translateUsecase.getText(model.message, lang, ulang) { str ->
                                            model.translatedMessage = str
                                            _chatModelList.value = chatList
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }, list)

//            chatUsecase.getMessages({ chatList ->
//                _chatModelList.value = chatList
//
//                viewModelScope.launch {
//                    chatList.forEach { model ->
//                        viewModelScope.launch {
//                            checkLanguage(model.message) { lang ->
//                                viewModelScope.launch {
//                                    _language.value?.let { ulang ->
//                                        translateUsecase.getText(model.message, lang, ulang) { str ->
//                                            model.translatedMessage = str
//                                            val nList = mutableListOf<ChatModel>()
//                                            chatList.forEach { item -> nList.add(item.copy(translatedMessage = item.translatedMessage)) }
//                                            _chatModelList.value = nList
//
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }, list)
        }
    }

    suspend fun getTranslatedText(data: String, isReversed: Boolean?, tts: TextToSpeech?, callback: (String) -> Unit) {
        viewModelScope.launch {
            checkLanguage(data) { lang ->
                viewModelScope.launch {
                    _language.value?.let { ulang ->
                        if (isReversed != null && !isReversed) {
                            translateUsecase.getText(data, ulang, "ko") { str ->
                                tts?.apply {
                                    language = Locale.forLanguageTag(ulang)
                                    setPitch(1.0f)
                                    setSpeechRate(1.0f)
                                    speak(data, TextToSpeech.QUEUE_FLUSH, null)
                                }

                                callback(str)
                            }
                        } else if (isReversed != null && isReversed) {
                            translateUsecase.getText(data, "ko", ulang) { str ->
                                tts?.apply {
                                    language = Locale.forLanguageTag("ko")
                                    setPitch(1.0f)
                                    setSpeechRate(1.0f)
                                    speak(data, TextToSpeech.QUEUE_FLUSH, null)
                                }
                                callback(str)
                            }
                        } else {
                            translateUsecase.getText(data, lang, ulang) { str ->
                                callback(str)
                            }
                        }
                    }
                }
            }
        }
    }
}