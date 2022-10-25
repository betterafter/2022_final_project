package com.example.kudata.repository

import com.example.kudata.entity.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    suspend fun initUserInfo()

    suspend fun getUser(callback: (User) -> Unit)

    suspend fun updateUserInfo(
        userUid: String?,
        userName: String?,
        userEmail: String?,
        userRank: String?,
        userXp: Int?
    )
    //fun updateUserRef(firebaseUser: FirebaseUser)
}