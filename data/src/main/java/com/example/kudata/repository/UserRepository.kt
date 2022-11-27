package com.example.kudata.repository

import android.net.Uri
import com.example.kudata.dto.User

interface UserRepository {

    suspend fun initUserInfo()

    suspend fun getUser(uid: String?, callback: (User) -> Unit)

    suspend fun getUser(uid: String?): User?

    suspend fun getUsers(): List<User>

    suspend fun updateUserInfo(
        userUid: String?,
        userName: String?,
        userEmail: String?,
        userRank: String?,
        userXp: Int?,
        language: String?,
        profile: Uri?,
        questionList: Map<String, Any>?,
        favoriteList: Map<String, Any>?,
    )
    //fun updateUserRef(firebaseUser: FirebaseUser)
}