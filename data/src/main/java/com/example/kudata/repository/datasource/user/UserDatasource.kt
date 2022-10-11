package com.example.kudata.repository.datasource.user

interface UserDatasource {
    suspend fun initUserInfo()
    suspend fun updateUserInfo(userUid: String?, userName: String?, userEmail: String?, userRank: String?, userXp: Int?)
}