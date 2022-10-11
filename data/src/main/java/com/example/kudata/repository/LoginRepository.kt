package com.example.kudata.repository

import android.content.Intent
import com.facebook.AccessToken
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

interface LoginRepository {
    suspend fun getUser(): FirebaseUser?
    // google
    suspend fun getGoogleSignInIntent(): Intent
    suspend fun googleLogin(data: Intent, callback: (FirebaseUser?) -> Unit)

    // facebook
    suspend fun loginWithFacebook(callback: ((LoginResult) -> Unit))
    fun handleFacebookAccessToken(accessToken: AccessToken, callback: (FirebaseUser?) -> Unit)
    suspend fun callbackManagerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}