package com.example.domain.repository

import android.content.Intent
import android.util.Log
import com.example.kudata.repository.LoginRepository
import com.example.kudata.repository.datasource.login.FacebookLoginDatasource
import com.example.kudata.repository.datasource.login.GoogleLoginDatasource
import com.facebook.AccessToken
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val googleLoginDatasource: GoogleLoginDatasource,
    private val facebookLoginDatasource: FacebookLoginDatasource
) : LoginRepository {
    override suspend fun checkAutoLoginUSer(): FirebaseUser? {
        return googleLoginDatasource.checkAutoLoginUSer()
    }

    override suspend fun getUser(): FirebaseUser? {
        if (googleLoginDatasource.getGoogleUser() != null)
            return googleLoginDatasource.getGoogleUser()
        if (facebookLoginDatasource.getFacebookUser() != null)
            return facebookLoginDatasource.getFacebookUser()
        return null
    }

    // google
    override suspend fun getGoogleSignInIntent(): Intent = googleLoginDatasource.getGoogleSignInIntent()

    override suspend fun googleLogin(data: Intent, callback: (FirebaseUser?) -> Unit) {
        googleLoginDatasource.googleLogin(data) {
            callback(it)
        }
    }

    // facebook
    override suspend fun loginWithFacebook(callback: ((LoginResult) -> Unit)) {
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