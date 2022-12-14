package com.example.kudata.repository.datasource.chat

import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom

interface ChatDataSource {
    suspend fun initChatRoom(qid: String, uid2: String?, isPrivate: Boolean, initialCallback: ((ChatRoom?) -> Unit))
    suspend fun initPublicChatRoom(qid: String, isPrivate: Boolean)
    suspend fun enterPublicRoom(qid: String, callback: () -> Unit)
    suspend fun getUserChatRoomsAsync(callback: (List<ChatRoom>, List<ChatRoom>) -> Unit)
    suspend fun sendMessage(message: String, timeStamp: Long)
    suspend fun getRealtimeMessage(updatedMessageCallback: ((Map<String, ChatContent>) -> Unit))
    suspend fun updateChat(
        qid: String?,
        private: Boolean?,
        end: Boolean?
    )
}