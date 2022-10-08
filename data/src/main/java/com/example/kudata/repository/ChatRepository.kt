package com.example.kudata.repository

interface ChatRepository {
    suspend fun initRoom(uid2: String)
    suspend fun enterRoom()
    suspend fun sendMessage(message: String, timeStamp: String)
}