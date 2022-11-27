package com.kuroutine.kulture.chatroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.ChatRoomModel
import com.example.domain.entity.DashboardQuestionModel
import com.example.domain.entity.UserModel
import com.example.domain.usecase.chat.ChatUsecase
import com.example.domain.usecase.dashboard.DashboardUsecase
import com.example.domain.usecase.papago.TranslateUsecase
import com.example.domain.usecase.user.UserUsecase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val dashboardUsecase: DashboardUsecase,
    private val chatUsecase: ChatUsecase,
    private val translateUsecase: TranslateUsecase,
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

    suspend fun getUser(uid: String): UserModel? {
        return userUsecase.getUser(uid)
    }

    suspend fun getQuestion(uid: String, callback: (DashboardQuestionModel) -> Unit) {
        val question = dashboardUsecase.getQuestion(uid)

        _language.value?.let { ulang ->
            question?.title?.let { title ->
                checkLanguage(title) { lang ->
                    viewModelScope.launch {
                        translateUsecase.getText(title, lang, ulang) { str ->
                            question.translatedTitle = str
                            callback(question)
                        }
                    }
                }
            }
        }
    }

    fun getChatRooms() {

        viewModelScope.launch {
            chatUsecase.getChatRooms { list1, list2 ->
                _chatRoomModelList.value = list1
                _publicChatRoomModelList.value = list2

                list1.forEachIndexed { index, model ->
                    viewModelScope.launch {
                        model.contents?.last()?.message?.let { it1 ->
                            _language.value?.let { ulang ->
                                checkLanguage(it1) { lang ->
                                    viewModelScope.launch {
                                        translateUsecase.getText(it1, lang, ulang) { str ->
                                            model.contents!!.last().message = str
                                            _chatRoomModelList.value = list1
                                        }
                                    }
                                }
                            } ?: run {
                                _chatRoomModelList.value = list1
                            }
                        }
                    }
                }

                list2.forEachIndexed { index, model ->
                    viewModelScope.launch {
                        model.contents?.last()?.message?.let { it1 ->
                            _language.value?.let { ulang ->
                                checkLanguage(it1) { lang ->
                                    viewModelScope.launch {
                                        translateUsecase.getText(it1, lang, ulang) { str ->
                                            model.contents!!.last().message = str
                                            _publicChatRoomModelList.value = list2
                                        }
                                    }
                                }
                            } ?: run {
                                _publicChatRoomModelList.value = list2
                            }
                        }
                    }
                }
            }
        }
    }

    suspend fun checkLanguage(data: String, callback: (String) -> Unit) {
        translateUsecase.getLangCode(data) {
            callback(it)
        }
    }

    suspend fun getLanguage() {
        userUsecase.getUser(null) {
            if (_language.value != it.language) {
                _language.value = it.language
            }
        }
    }

    suspend fun getTranslatedText(data: String, code: String, callback: (String) -> Unit) {
        translateUsecase.getText(data, code, _language.value ?: "ko") {
            callback(it)
        }
    }
}