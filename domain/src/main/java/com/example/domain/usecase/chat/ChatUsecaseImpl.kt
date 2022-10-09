package com.example.domain.usecase.chat

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.DtoTranslator
import com.example.domain.dto.Chat
import com.example.kudata.repository.ChatRepository
import com.example.kudata.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ChatUsecaseImpl @Inject constructor(
    private val loginRepository: LoginRepository,
    private val chatRepository: ChatRepository
) : ChatUsecase {
    override suspend fun getCurrentUser(): FirebaseUser? {
        return loginRepository.getUser()
    }

    override suspend fun initRoom(uid: String) {
        chatRepository.initRoom(uid)
    }

    override suspend fun enterRoom() {
        chatRepository.enterRoom()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun sendMessage(message: String) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val timestamp = current.format(formatter)

        chatRepository.sendMessage(message, timestamp)
    }

    override suspend fun getMessages(callback: (List<Chat>) -> Unit) {
        chatRepository.getRealtimeMessage {
            val list = DtoTranslator.chatTranslator(it)
            callback(list)
        }
    }
}