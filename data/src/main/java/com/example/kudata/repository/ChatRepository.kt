package com.example.kudata.repository

import com.example.kudata.entity.ChatContent

interface ChatRepository {
    suspend fun initRoom(uid2: String, initialCallback: (() -> Unit))
    suspend fun enterRoom()
    suspend fun sendMessage(message: String, timeStamp: String)
    suspend fun getRealtimeMessage(getListCallback: (List<ChatContent>) -> Unit)
}