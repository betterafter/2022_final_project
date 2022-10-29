package com.example.domain.usecase.user

import com.example.domain.dto.UserModel

interface UserUsecase {
    suspend fun initUser()
    suspend fun getUser(callback: (UserModel) -> Unit)
    suspend fun updateLanguage(lang: String)
}