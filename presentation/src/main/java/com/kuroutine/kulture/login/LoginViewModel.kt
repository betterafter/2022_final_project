package com.kuroutine.kulture.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.login.LoginUsecase
import com.example.domain.usecase.user.UserUsecase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUsecase: LoginUsecase,
    private val userUsercas: UserUsecase,
) : ViewModel() {
    private val _currentUser = MutableLiveData<FirebaseUser?>().apply { value = null }
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _googleSignInIntent = MutableLiveData<Intent?>().apply { value = null }
    val googleSingInIntent: LiveData<Intent?> = _googleSignInIntent

    val permissionList: MutableList<String> = listOf("public_profile", "email") as MutableList<String>

    fun getGoogleSignInIntent() {
        viewModelScope.launch {
            _googleSignInIntent.value = loginUsecase.getGoogleSignInIntent()
        }
    }

    fun googleLogin(data: Intent?) {
        if (data == null) {
            // TODO : toast 메세지로 '로그인에 실패했다' 는 메세지 띄울 것
            return
        }

        viewModelScope.launch {
            loginUsecase.googleLogin(data) {
                _currentUser.value = it
                viewModelScope.launch { userUsercas.initUser() }
            }
        }
    }

    fun callbackManagerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModelScope.launch {
            loginUsecase.callbackManagerOnActivityResult(requestCode, resultCode, data)
        }
    }

    fun loginWithFacebook() {
        viewModelScope.launch {
            loginUsecase.loginWithFacebook(callback = {
                _currentUser.value = it
                viewModelScope.launch { userUsercas.initUser() }
            })
        }
    }
}