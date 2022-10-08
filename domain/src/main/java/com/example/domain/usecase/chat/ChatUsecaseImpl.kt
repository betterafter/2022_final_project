package com.example.domain.usecase.chat

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.kudata.repository.ChatRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ChatUsecaseImpl @Inject constructor(
    private val chatRepository: ChatRepository
) : ChatUsecase {
    suspend fun initRoom(uid: String) {
        chatRepository.initRoom(uid)
    }

    suspend fun enterRoom() {
        chatRepository.enterRoom()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun sendMessage(message: String) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_DATE
        val timestamp = current.format(formatter)

        chatRepository.sendMessage(message, timestamp)
    }
}