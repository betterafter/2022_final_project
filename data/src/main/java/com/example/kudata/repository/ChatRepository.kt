package com.example.kudata.repository

import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom
import com.example.kudata.entity.FcmResponse

interface ChatRepository {
    suspend fun initRoom(qid: String, uid2: String?, isPrivate: Boolean, initialCallback: (() -> Unit))
    suspend fun enterRoom(qid: String, getChatRoomIdCallback: ((String?) -> Unit))
    suspend fun getChatRooms(callback: (List<ChatRoom>, List<ChatRoom>) -> Unit)
    suspend fun sendMessage(message: String, timeStamp: Long)
    suspend fun getRealtimeMessage(getListCallback: (Map<String, ChatContent>) -> Unit)
    suspend fun sendPushMessage(
        to: String,
        title: String,
        body: String,
        qid: String,
        uid: String,
        userProfile: String,
        isPrivate: Boolean
    ): FcmResponse?
}