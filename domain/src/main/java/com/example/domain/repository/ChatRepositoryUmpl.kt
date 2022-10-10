package com.example.domain.repository

import com.example.domain.DtoTranslator
import com.example.domain.dto.Chat
import com.example.kudata.entity.ChatContent
import com.example.kudata.repository.ChatRepository
import com.example.kudata.repository.datasource.chat.ChatDataSource
import javax.inject.Inject

class ChatRepositoryUmpl @Inject constructor(
    private val chatDataSource: ChatDataSource
) : ChatRepository {

    // 채팅방 초기화. 사용자의 uid 기준으로 채팅방 생성
    override suspend fun initRoom(uid2: String) {
        chatDataSource.initChatRoom(uid2)
    }

    override suspend fun enterRoom() {

    }

    override suspend fun sendMessage(message: String, timeStamp: String) {
        chatDataSource.sendMessage(message, timeStamp)
    }

    override suspend fun getRealtimeMessage(getListCallback: (List<ChatContent>) -> Unit) {
        chatDataSource.getRealtimeMessage {
            getListCallback(it)
        }
    }
}