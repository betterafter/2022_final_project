package com.example.kudata.repository

import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom

interface ChatRepository {
    suspend fun initRoom(qid: String, uid2: String, initialCallback: (() -> Unit))
    suspend fun enterRoom()
    suspend fun getChatRooms(callback: (List<ChatRoom>) -> Unit)
    suspend fun sendMessage(message: String, timeStamp: Long)
    suspend fun getRealtimeMessage(getListCallback: (Map<String, ChatContent>) -> Unit)
}