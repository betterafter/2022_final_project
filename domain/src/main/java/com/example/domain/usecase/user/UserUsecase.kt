package com.example.domain.usecase.user

import android.net.Uri
import com.example.domain.dto.UserModel

interface UserUsecase {
    suspend fun initUser()
    suspend fun getUser(callback: (UserModel) -> Unit)
    suspend fun updateLanguage(lang: String)
    suspend fun setUserProfileImage(uri: Uri)
}