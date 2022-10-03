package com.example.domain.repository

import android.content.Intent
import android.util.Log
import com.example.kudata.repository.LoginRepository
import com.example.kudata.repository.datasource.login.FacebookLoginDatasource
import com.facebook.AccessToken
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val facebookLoginDatasource: FacebookLoginDatasource
) : LoginRepository {
    override suspend fun loginWithEmail() {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithFacebook(callback: ((LoginResult)->Unit)) {
        CoroutineScope(Dispatchers.IO).launch {
            facebookLoginDatasource.login { loginResult ->
                callback(loginResult)
            }
        }
    }

    override fun handleFacebookAccessToken(accessToken: AccessToken, callback: (FirebaseUser?) -> Unit) {
        facebookLoginDatasource.handleFacebookAccessToken(accessToken, callback)
    }

    override suspend fun callbackManagerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookLoginDatasource.callbackManagerOnActivityResult(requestCode, resultCode, data)
    }
}