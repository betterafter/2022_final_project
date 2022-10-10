package com.example.kudata.repository.datasource.chat

import com.example.kudata.entity.ChatContent

interface ChatDataSource {
    suspend fun initChatRoom(uid2: String, initialCallback: (() -> Unit))
    suspend fun enterRoom()
    suspend fun sendMessage(message: String, timeStamp: String)
    suspend fun getRealtimeMessage(updatedMessageCallback: ((List<ChatContent>) -> Unit))
}