package com.example.domain.usecase.chat

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.domain.DtoTranslator
import com.example.domain.dto.ChatModel
import com.example.domain.dto.ChatRoomModel
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

    override suspend fun initRoom(qid: String, uid: String?, isPrivate: Boolean, initialCallback: (() -> Unit)) {
        chatRepository.initRoom(qid, uid, isPrivate) {
            initialCallback()
        }
    }

    override suspend fun enterRoom(qid: String, getChatRoomIdCallback: ((String?) -> Unit)) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun sendMessage(message: String) {
        val timestamp = System.currentTimeMillis()
        chatRepository.sendMessage(message, timestamp)
    }

    override suspend fun getMessages(callback: (List<ChatModel>) -> Unit) {
        chatRepository.getRealtimeMessage {
            val list = DtoTranslator.chatTranslator(it)
            callback(list)
        }
    }

    override suspend fun getChatRooms(callback: (List<ChatRoomModel>, List<ChatRoomModel>) -> Unit) {
        chatRepository.getChatRooms { list1, list2 ->
            callback(DtoTranslator.chatRoomsTranslator(list1), DtoTranslator.chatRoomsTranslator(list2))
        }
    }
}