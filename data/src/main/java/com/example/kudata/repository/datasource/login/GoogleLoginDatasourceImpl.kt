package com.example.kudata.repository.datasource.login

import android.R.attr.data
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.kudata.R
import com.example.kudata.entity.FirebaseExecutor
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class GoogleLoginDatasourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GoogleLoginDatasource {
    private val firebaseAuth = Firebase.auth

    override suspend fun getGoogleUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }


    override suspend fun getGoogleSignInIntent(): Intent {
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        return mGoogleSignInClient.signInIntent
    }

    override suspend fun googleLogin(data: Intent, callback: (FirebaseUser?) -> Unit) {
        val result: GoogleSignInResult? = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        if (result == null) {
            callback(null)
            return
        }

        if (result.isSuccess) {
            val account: GoogleSignInAccount? = result.signInAccount
            if (account == null) {
                callback(null)
                return
            }

            firebaseAuthWithGoogle(account) {
                callback(it)
            }
        } else {
            //구글 로그인 실패
        }
    }

    override suspend fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, callback: (FirebaseUser?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(
                FirebaseExecutor()
            ) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    callback(user)
                } else {

                }
            }
    }
}