package com.example.domain.usecase.chat

import com.example.domain.dto.ChatModel
import com.example.domain.dto.ChatRoomModel
import com.google.firebase.auth.FirebaseUser

interface ChatUsecase {
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun initRoom(qid: String, uid: String?, isPrivate: Boolean, initialCallback: (() -> Unit))
    suspend fun enterRoom(qid: String, getChatRoomIdCallback: ((String?) -> Unit))
    suspend fun getChatRooms(callback: (List<ChatRoomModel>, List<ChatRoomModel>) -> Unit)
    suspend fun sendMessage(message: String)
    suspend fun getMessages(callback: (List<ChatModel>) -> Unit, compList: List<ChatModel>?)
}