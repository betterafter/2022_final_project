package com.example.kudata.repository.datasource.chat

import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom

interface ChatDataSource {
    suspend fun initChatRoom(qid: String, uid2: String, initialCallback: (() -> Unit))
    suspend fun enterRoom()
    suspend fun getUserChatRoomsAsync(callback: (List<ChatRoom>) -> Unit)
    suspend fun sendMessage(message: String, timeStamp: Long)
    suspend fun getRealtimeMessage(updatedMessageCallback: ((Map<String, ChatContent>) -> Unit))
}