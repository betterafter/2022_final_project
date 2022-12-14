package com.example.domain.repository

import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom
import com.example.kudata.entity.FcmResponse
import com.example.kudata.repository.ChatRepository
import com.example.kudata.repository.datasource.chat.ChatDataSource
import com.example.kudata.repository.datasource.fcm.FcmDatasource
import javax.inject.Inject

class ChatRepositoryUmpl @Inject constructor(
    private val chatDataSource: ChatDataSource,
    private val fcmDatasource: FcmDatasource
) : ChatRepository {

    // 채팅방 초기화. 사용자의 uid 기준으로 채팅방 생성
    override suspend fun initRoom(qid: String, uid2: String?, isPrivate: Boolean, initialCallback: ((ChatRoom?) -> Unit)) {
        chatDataSource.initChatRoom(qid, uid2, isPrivate) {
            initialCallback(it)
        }
    }

    override suspend fun enterRoom(qid: String, getChatRoomIdCallback: ((String?) -> Unit)) {

    }

    override suspend fun getChatRooms(callback: (List<ChatRoom>, List<ChatRoom>) -> Unit) {
        chatDataSource.getUserChatRoomsAsync { list1, list2 ->
            callback(list1, list2)
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

    override suspend fun sendPushMessage(
        to: String,
        title: String,
        body: String,
        qid: String,
        uid: String,
        users: List<String>,
        userProfile: String,
        isPrivate: Boolean
    ): FcmResponse? {
        return fcmDatasource.getFcmResult(to, title, body, qid, uid, users, userProfile, isPrivate)
    }

    override suspend fun updateChat(
        qid: String?,
        private: Boolean?,
        end: Boolean?
    ) {
        chatDataSource.updateChat(qid, private, end)
    }
}