package com.example.domain.usecase.login

import android.content.Intent
import android.util.Log
import com.example.kudata.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class LoginUsecaseImpl @Inject constructor(
    private val loginRepository: LoginRepository
) : LoginUsecase {
    override suspend fun loginWithEmail() {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithFacebook(callback: ((FirebaseUser?)->Unit)) {
        loginRepository.loginWithFacebook { loginResult ->
            loginRepository.handleFacebookAccessToken(
                loginResult.accessToken,
                object : ((FirebaseUser?)->Unit) {
                    override fun invoke(firebaseUser: FirebaseUser?) {
                        callback(firebaseUser)
                    }
                }
            )
        }
    }

    override suspend fun callbackManagerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loginRepository.callbackManagerOnActivityResult(requestCode, resultCode, data)
    }
}