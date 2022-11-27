package com.kuroutine.kulture.mypage

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.LanguageModel
import com.example.domain.entity.UserModel
import com.example.domain.usecase.dashboard.DashboardUsecase
import com.example.domain.usecase.user.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userUsecase: UserUsecase,
    private val dashboardUsecase: DashboardUsecase
) : ViewModel() {
    private val _currentUser = MutableLiveData<UserModel?>().apply { value = null }
    val currentUser: LiveData<UserModel?> = _currentUser

    fun getUser() {
        viewModelScope.launch {
            userUsecase.getUser(null) {
                _currentUser.value = it
            }
        }
    }

    fun updateQuestionState(qid: String, questionState: String) {
        viewModelScope.launch {
            dashboardUsecase.updateQuestionState(qid, questionState)
        }
    }

    fun updateUserLanguage(model: LanguageModel) {
        viewModelScope.launch {
            userUsecase.updateLanguage(model.code)
            getUser()
        }
    }

    fun updateUserProfile(uri: Uri) {
        viewModelScope.launch {
            userUsecase.setUserProfileImage(uri)
            getUser()
        }
    }
}