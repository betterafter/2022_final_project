package com.example.domain.usecase.chat

import com.example.domain.dto.Chat
import com.google.firebase.auth.FirebaseUser

interface ChatUsecase {
    suspend fun getCurrentUser() : FirebaseUser?
    suspend fun initRoom(uid: String, initialCallback: (() -> Unit))
    suspend fun enterRoom()
    suspend fun sendMessage(message: String)
    suspend fun getMessages(callback: (List<Chat>) -> Unit)
}