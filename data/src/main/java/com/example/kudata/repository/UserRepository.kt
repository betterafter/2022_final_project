package com.example.kudata.repository

import android.net.Uri
import com.example.kudata.entity.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    suspend fun initUserInfo()

    suspend fun getUser(uid: String?, callback: (User) -> Unit)

    suspend fun getUser(uid: String?): User?

    suspend fun getUsers()

    suspend fun updateUserInfo(
        userUid: String?,
        userName: String?,
        userEmail: String?,
        userRank: String?,
        userXp: Int?,
        language: String?,
        profile: Uri?
    )
    //fun updateUserRef(firebaseUser: FirebaseUser)
}