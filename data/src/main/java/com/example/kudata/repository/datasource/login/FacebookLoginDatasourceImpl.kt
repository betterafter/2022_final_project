package com.example.kudata.repository.datasource.login

import android.content.Intent
import android.util.Log
import com.example.kudata.entity.FirebaseExecutor
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FacebookLoginDatasourceImpl : FacebookLoginDatasource {
    private val callbackManager = CallbackManager.Factory.create()
    private val loginManager = LoginManager.getInstance()
    private val firebaseAuth = Firebase.auth

    override suspend fun getFacebookUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun login(callback : ((LoginResult)->Unit)) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                Log.d("[keykat]", "login canceled.")
            }

            override fun onError(error: FacebookException) {
                Log.d("[keykat]", "error occured: $error")
            }

            override fun onSuccess(result: LoginResult) {
                Log.d("[keykat]", "succes: $result")
                callback(result)
            }
        })
    }

    override fun handleFacebookAccessToken(
        accessToken: AccessToken,
        callback: (FirebaseUser?) -> Unit
    ) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(FirebaseExecutor()) { task ->
                if (task.isSuccessful) {
                    callback(firebaseAuth.currentUser)
                } else {
                    callback(null)
                }
            }
    }

    override suspend fun callbackManagerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}