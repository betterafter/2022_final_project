package com.example.domain.usecase.login

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

interface LoginUsecase {
    // google
    suspend fun getGoogleSignInIntent() : Intent
    suspend fun googleLogin(data: Intent, callback: (FirebaseUser?) -> Unit)

    // facebook
    suspend fun loginWithFacebook(callback: ((FirebaseUser?)->Unit))
    suspend fun  callbackManagerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}