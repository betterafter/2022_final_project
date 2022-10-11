package com.example.kudata.repository

import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    suspend fun initUserInfo()

    suspend fun updateUserInfo(
        userUid: String?,
        userName: String?,
        userEmail: String?,
        userRank: String?,
        userXp: Int?
    )
    //fun updateUserRef(firebaseUser: FirebaseUser)
}