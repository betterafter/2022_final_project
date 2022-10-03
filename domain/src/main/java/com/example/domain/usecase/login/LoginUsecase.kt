package com.example.domain.usecase.login

import android.content.Intent
import com.google.firebase.auth.FirebaseUser

interface LoginUsecase {
    suspend fun loginWithEmail()
    suspend fun loginWithFacebook() : FirebaseUser?
    suspend fun  callbackManagerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}