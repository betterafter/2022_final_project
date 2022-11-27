package com.example.domain.usecase.login

import android.R.attr.data
import android.content.Intent
import android.util.Log
import com.example.kudata.repository.LoginRepository
import com.example.kudata.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class LoginUsecaseImpl @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository
) : LoginUsecase {
    override suspend fun checkAutoLoginUSer(): FirebaseUser? {
        return loginRepository.checkAutoLoginUSer()
    }
    // google
    override suspend fun getGoogleSignInIntent() : Intent = loginRepository.getGoogleSignInIntent()

    override suspend fun googleLogin(data: Intent, callback: (FirebaseUser?) -> Unit) {
        loginRepository.googleLogin(data) {
            callback(it)
        }
    }

    // facebook
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