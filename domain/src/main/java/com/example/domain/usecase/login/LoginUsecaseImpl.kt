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

    override suspend fun loginWithFacebook(): FirebaseUser? {
        Log.d("[keykat]"," !!!!!!!!!!!!!!!!!")
        var user: FirebaseUser? = null
        loginRepository.loginWithFacebook { loginResult ->
            loginRepository.handleFacebookAccessToken(
                loginResult.accessToken,
                object : ((FirebaseUser?)->Unit) {
                    override fun invoke(firebaseUser: FirebaseUser?) {
                        Log.d("[keykat]", "fireaseUser: " + firebaseUser.toString())
                        user = firebaseUser
                    }
                }
            )
        }
        Log.d("[keykat]", "user: " + user.toString())
        return user
    }

    override suspend fun callbackManagerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loginRepository.callbackManagerOnActivityResult(requestCode, resultCode, data)
    }
}