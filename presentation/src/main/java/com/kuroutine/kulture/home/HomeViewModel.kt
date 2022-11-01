package com.kuroutine.kulture.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.usecase.dashboard.DashboardUsecase
import com.example.domain.usecase.papago.PapagoUsecase
import com.example.domain.usecase.user.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dashboardUsecase: DashboardUsecase,
    private val userUsecase: UserUsecase,
    private val papagoUsecase: PapagoUsecase
) : ViewModel() {
    private val _language = MutableLiveData<String?>().apply {
        value = null
    }
    val language: LiveData<String?> = _language

    private val _questionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val questionList: LiveData<List<DashboardQuestionModel>?> = _questionList

    private val _searchedQuestionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val searchedQuestionList: LiveData<List<DashboardQuestionModel>?> = _searchedQuestionList

    // 사용자에 맞는 언어로 실시간 변환
    suspend fun getQuestions() {
        val list = _questionList.value

        withContext(viewModelScope.coroutineContext) {
            getLanguage()
        }

        viewModelScope.launch {
            dashboardUsecase.getQuestionsInRealtime(list) {
                _questionList.value = it
                viewModelScope.launch {
                    // updateTranslatedQuestionList()
                }
            }
        }
    }

    suspend fun getLanguage() {
        userUsecase.getUser {
            if (_language.value != it.language) {
                resetTranslateState()
                _language.value = it.language
            }
        }
    }

    suspend fun checkLanguage(data: String): String {
        return papagoUsecase.getLangCode(data) ?: "ko"
    }

    suspend fun getTranslatedText(data: String, code: String): String? {
        return papagoUsecase.getText(data, code, _language.value ?: "ko")
    }

    suspend fun updateTranslatedQuestionList() {
        val list = mutableListOf<DashboardQuestionModel>()
        _questionList.value?.forEach { model ->
            val text = model.title
            if (!model.translatedState) {
                val langCode = papagoUsecase.getLangCode(text) ?: "ko"
                // 소스 언어와 타켓 언어가 같으면 에러 발생 -> 그냥 원본 값 넣는다
                if (langCode == _language.value) {
                    list.add(model.copy(translatedState = true))
                } else {
                    val newTitle = papagoUsecase.getText(model.title, langCode, _language.value ?: "ko")
                    val newText = papagoUsecase.getText(model.text, langCode, _language.value ?: "ko")
                    val newLocation = papagoUsecase.getText(model.location, langCode, _language.value ?: "ko")

                    val newModel = model.copy(
                        title = newTitle ?: "",
                        text = newText ?: "",
                        location = newLocation ?: "",
                        translatedState = true
                    )

                    list.add(newModel)
                }
            } else {
                list.add(model.copy(translatedState = true))
            }
            _questionList.value = list
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