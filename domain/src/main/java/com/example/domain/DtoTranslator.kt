package com.example.domain

import com.example.domain.dto.Chat
import com.example.kudata.entity.ChatContent

object DtoTranslator {
    fun chatTranslator(chatList: List<ChatContent>) : List<Chat> {
        val list = mutableListOf<Chat>()
        chatList.forEach {
            list.add(Chat(it.uid, it.message, it.timestamp))
        }

        return list
    }
}