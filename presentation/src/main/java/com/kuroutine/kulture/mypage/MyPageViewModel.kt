package com.kuroutine.kulture.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.LanguageModel
import com.example.domain.dto.UserModel
import com.example.domain.usecase.user.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userUsecase: UserUsecase
) : ViewModel() {
    private val _currentUser = MutableLiveData<UserModel?>().apply { value = null }
    val currentUser: LiveData<UserModel?> = _currentUser

    private val _languageList = MutableLiveData<List<LanguageModel>?>().apply { value = null }
    val languageList: LiveData<List<LanguageModel>?> = _languageList

    fun getUser() {
        viewModelScope.launch {
            userUsecase.getUser {
                _currentUser.value = it
            }
        }
    }

    fun setLanguageList() {
        _languageList.value = listOf(
            LanguageModel("ko", "한국어"),
            LanguageModel("ja", "일본어"),
            LanguageModel("zh-CN","중국어 (간체)"),
            LanguageModel("zh-TW", "중국어 (번체)"),
            LanguageModel("hi", "힌디어"),
            LanguageModel("en", "영어"),
            LanguageModel("es", "스페인어"),
            LanguageModel("fr", "프랑스어"),
            LanguageModel("de", "독일어"),
            LanguageModel("pt", "포르투칼어"),
            LanguageModel("vi", "베트남어"),
            LanguageModel("id", "인도네시아어"),
            LanguageModel("fa", "페르시아어"),
            LanguageModel("ar", "아랍어"),
            LanguageModel("mm", "미얀마어"),
            LanguageModel("th", "태국어"),
            LanguageModel("ru", "러시아어"),
            LanguageModel("it", "이탈리아어"),
        )
    }
}