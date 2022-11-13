package com.example.kudata.repository.datasource.user

import android.net.Uri
import com.example.kudata.entity.User

interface UserDatasource {
    suspend fun initUserInfo()
    suspend fun getUserInfo(uid: String?, callback: (User) -> Unit)
    suspend fun getUserInfo(uid: String?): User?
    suspend fun getUsersInfo(): List<User>
    suspend fun updateUserInfo(
        userUid: String?,
        userName: String?,
        userEmail: String?,
        userRank: String?,
        userXp: Int?,
        language: String?,
        profile: Uri?
    )
}