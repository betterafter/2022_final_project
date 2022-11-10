package com.example.domain.repository

import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom
import com.example.kudata.repository.ChatRepository
import com.example.kudata.repository.datasource.chat.ChatDataSource
import javax.inject.Inject

class ChatRepositoryUmpl @Inject constructor(
    private val chatDataSource: ChatDataSource
) : ChatRepository {

    // 채팅방 초기화. 사용자의 uid 기준으로 채팅방 생성
    override suspend fun initRoom(qid: String, uid2: String, isPrivate: Boolean, initialCallback: (() -> Unit)) {
        chatDataSource.initChatRoom(qid, uid2, isPrivate) {
            initialCallback()
        }
    }

    override suspend fun enterRoom() {

    }

    override suspend fun getChatRooms(callback: (List<ChatRoom>) -> Unit) {
        chatDataSource.getUserChatRoomsAsync {
            callback(it)
        }
    }

    override suspend fun sendMessage(message: String, timeStamp: Long) {
        chatDataSource.sendMessage(message, timeStamp)
    }

    override suspend fun getRealtimeMessage(getListCallback: (Map<String, ChatContent>) -> Unit) {
        chatDataSource.getRealtimeMessage {
            getListCallback(it)
        }
    }
}