package com.kuroutine.kulture.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getUser() {
        viewModelScope.launch {
            userUsecase.getUser {
                _currentUser.value = it
            }
        }
    }
}