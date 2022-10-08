package com.example.kudata.repository.datasource.chat

interface ChatDataSource {
    suspend fun initChatRoom(uid2: String)
    suspend fun enterRoom()
    suspend fun sendMessage(message: String, timeStamp: String)
}