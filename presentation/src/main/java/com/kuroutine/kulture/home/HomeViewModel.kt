package com.kuroutine.kulture.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.dto.UserModel
import com.example.domain.usecase.dashboard.DashboardUsecase
import com.example.domain.usecase.papago.TranslateUsecase
import com.example.domain.usecase.user.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _searchedQuestionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val searchedQuestionList: LiveData<List<DashboardQuestionModel>?> = _searchedQuestionList

    private val _searchedPublicQuestionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val searchedPublicQuestionList: LiveData<List<DashboardQuestionModel>?> = _searchedPublicQuestionList

    private val _currentUser = MutableLiveData<UserModel?>().apply {
        value = null
    }
    val currentUser: LiveData<UserModel?> = _currentUser

    // 사용자에 맞는 언어로 실시간 변환
    suspend fun getQuestions() {
        val list = _questionList.value
        val list2 = _publicQuestionList.value

        withContext(viewModelScope.coroutineContext) {
            getLanguage()
        }

        viewModelScope.launch {
            dashboardUsecase.getQuestionsInRealtime(list, list2, callback = { list1 ->
                _questionList.value = list1
            }, callback2 = { list2 ->
                _publicQuestionList.value = list2
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
        _questionList.value?.forEach { model ->
            if (model.title.contains(title)) {
                list.add(model)
            }
        }
        _searchedQuestionList.value = list.toList()
    }

    fun resetTranslateState() {
        viewModelScope.launch {
            _questionList.value?.forEach {
                it.translatedState = false
            }
        }
    }
}