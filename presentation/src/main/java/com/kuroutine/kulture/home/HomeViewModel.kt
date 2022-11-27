package com.kuroutine.kulture.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.DashboardQuestionModel
import com.example.domain.entity.UserModel
import com.example.domain.usecase.dashboard.DashboardUsecase
import com.example.domain.usecase.papago.TranslateUsecase
import com.example.domain.usecase.user.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dashboardUsecase: DashboardUsecase,
    private val userUsecase: UserUsecase,
    private val translateUsecase: TranslateUsecase
) : ViewModel() {
    private val _language = MutableLiveData<String?>().apply {
        value = null
    }
    val language: LiveData<String?> = _language

    private val _questionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val questionList: LiveData<List<DashboardQuestionModel>?> = _questionList

    private val _publicQuestionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val publicQuestionList: LiveData<List<DashboardQuestionModel>?> = _publicQuestionList

    private val _publicDoneQuestionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val publicDoneQuestionList: LiveData<List<DashboardQuestionModel>?> = _publicDoneQuestionList


    private val _publicProcessingQuestionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val publicProcessingQuestionList: LiveData<List<DashboardQuestionModel>?> = _publicProcessingQuestionList


    private val _searchedQuestionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val searchedQuestionList: LiveData<List<DashboardQuestionModel>?> = _searchedQuestionList

    private val _publicQuestionState = MutableLiveData<String>().apply {
        value = "질문 중"
    }
    val publicQuestionState: LiveData<String?> = _publicQuestionState


    private val _searchedPublicQuestionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val searchedPublicQuestionList: LiveData<List<DashboardQuestionModel>?> = _searchedPublicQuestionList

    private val _currentUser = MutableLiveData<UserModel?>().apply {
        value = null
    }
    val currentUser: LiveData<UserModel?> = _currentUser

    fun setPublicQuestionState(state: String) {
        _publicQuestionState.value = state
    }

    fun setReversedPublicDoneList() {
        _publicDoneQuestionList.value = _publicDoneQuestionList.value?.reversed()
    }

    fun setReversedPublicProcessingList() {
        _publicProcessingQuestionList.value = _publicProcessingQuestionList.value?.reversed()
    }

    fun setReversedPrivateList() {
        val li = mutableListOf<DashboardQuestionModel>()
        _questionList.value?.forEach { li.add(it) }
        _questionList.value = null
        _questionList.value = li.reversed()
    }

    // 사용자에 맞는 언어로 실시간 변환
    suspend fun getQuestions() {
        val listA = _questionList.value
        val listB = _publicQuestionList.value

        viewModelScope.launch {
            dashboardUsecase.getQuestionsInRealtime(listA, listB, callback = { list1 ->
                list1?.forEach { model ->
                    viewModelScope.launch {
                        checkLanguage(model.title) { lang ->
                            viewModelScope.launch {
                                _language.value?.let { ulang ->
                                    translateUsecase.getText(model.title, lang, ulang) { str ->
                                        model.translatedTitle = str
                                        val nList = mutableListOf<DashboardQuestionModel>()
                                        list1.forEach { item -> nList.add(item.copy(translatedTitle = item.translatedTitle)) }
                                        _questionList.value = nList
                                    }
                                } ?: run {
                                    model.translatedTitle = model.title
                                    val nList = mutableListOf<DashboardQuestionModel>()
                                    list1.forEach { item -> nList.add(item.copy(translatedTitle = item.translatedTitle)) }
                                    _questionList.value = nList
                                }
                            }
                        }
                    }
                }
            }, callback2 = { list2 ->
                list2?.forEach { model ->
                    viewModelScope.launch {
                        checkLanguage(model.title) { lang ->
                            viewModelScope.launch {
                                _language.value?.let { ulang ->
                                    translateUsecase.getText(model.title, lang, ulang) { str ->
                                        model.translatedTitle = str
                                        val doneList = mutableListOf<DashboardQuestionModel>()
                                        val processingList = mutableListOf<DashboardQuestionModel>()

                                        list2.forEach { item ->
                                            if (item.questionState == "질문 중") {
                                                processingList.add(item.copy(translatedTitle = item.translatedTitle))
                                                _publicProcessingQuestionList.value = processingList
                                            } else if (item.questionState == "질문 완료") {
                                                doneList.add(item.copy(translatedTitle = item.translatedTitle))
                                                _publicDoneQuestionList.value = doneList
                                            }
                                        }
                                    }
                                } ?: run {
                                    model.translatedTitle = model.title
                                    val doneList = mutableListOf<DashboardQuestionModel>()
                                    val processingList = mutableListOf<DashboardQuestionModel>()

                                    list2.forEach { item ->
                                        if (item.questionState == "질문 중") {
                                            processingList.add(item.copy(translatedTitle = item.translatedTitle))
                                            _publicProcessingQuestionList.value = processingList
                                        } else if (item.questionState == "질문 완료") {
                                            doneList.add(item.copy(translatedTitle = item.translatedTitle))
                                            _publicDoneQuestionList.value = doneList
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    suspend fun getCurrentUser() {
        viewModelScope.launch {
            userUsecase.getUser(null) {
                _currentUser.value = it
            }
        }
    }

    suspend fun getUserProfile(uid: String, callback: (String) -> Unit) {
        viewModelScope.launch {
            userUsecase.getUser(uid) {
                callback(it.profile)
            }
        }
    }

    suspend fun getLanguage() {
        userUsecase.getUser(null) {
            if (_language.value != it.language) {
                resetTranslateState()
                _language.value = it.language
            }
        }
    }

    suspend fun checkLanguage(data: String, callback: (String) -> Unit) {
        translateUsecase.getLangCode(data) {
            callback(it)
        }
    }

    suspend fun getTranslatedText(data: String, code: String, callback: (String) -> Unit) {
        translateUsecase.getText(data, code, _language.value ?: "ko") {
            callback(it)
        }
    }

    fun updateSearchedList(title: String) {
        val list: MutableList<DashboardQuestionModel> = mutableListOf()
        val publicList: MutableList<DashboardQuestionModel> = mutableListOf()
        _questionList.value?.forEach { model ->
            if (model.translatedTitle.lowercase(Locale.ROOT).contains(title.lowercase(Locale.ROOT))) {
                list.add(model)
            }
        }

        _publicQuestionList.value?.forEach { model ->
            if (model.translatedTitle.lowercase(Locale.ROOT).contains(title.lowercase(Locale.ROOT))) {
                publicList.add(model)
            }
        }

        _searchedQuestionList.value = list.toList()
        _searchedPublicQuestionList.value = publicList.toList()
    }

    fun resetTranslateState() {
        viewModelScope.launch {
            _questionList.value?.forEach {
                it.translatedState = false
            }
        }
    }
}