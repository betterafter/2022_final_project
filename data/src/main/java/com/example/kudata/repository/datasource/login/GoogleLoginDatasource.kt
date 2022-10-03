package com.example.kudata.repository.datasource.login

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

interface GoogleLoginDatasource {
    suspend fun getGoogleSignInIntent(): Intent
    suspend fun googleLogin(data: Intent, callback: (FirebaseUser?) -> Unit)
    suspend fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, callback: (FirebaseUser?) -> Unit)
}