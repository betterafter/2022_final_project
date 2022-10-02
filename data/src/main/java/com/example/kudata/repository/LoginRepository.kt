package com.example.kudata.repository

interface LoginRepository {
    suspend fun loginWithEmail()
    suspend fun loginWithFacebook()
}