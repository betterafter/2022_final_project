package com.example.domain.usecase.login

interface LoginUsecase {
    suspend fun loginWithEmail()
    suspend fun loginWithFacebook()
}