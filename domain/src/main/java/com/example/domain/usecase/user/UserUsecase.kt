package com.example.domain.usecase.user

import android.net.Uri
import com.example.domain.dto.UserModel

interface UserUsecase {
    suspend fun initUser()
    suspend fun getUser(uid: String?, callback: (UserModel) -> Unit)
    suspend fun getUser(uid: String?): UserModel?
    suspend fun getUsers(): List<UserModel>
    suspend fun updateLanguage(lang: String)
    suspend fun setUserProfileImage(uri: Uri)
    suspend fun updateXp(uid: String, xp: Int)
}