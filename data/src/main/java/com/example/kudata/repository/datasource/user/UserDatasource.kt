package com.example.kudata.repository.datasource.user

import com.example.kudata.entity.User

interface UserDatasource {
    suspend fun initUserInfo()
    suspend fun getUserInfo(callback: (User) -> Unit)
    suspend fun updateUserInfo(userUid: String?, userName: String?, userEmail: String?, userRank: String?, userXp: Int?)
}