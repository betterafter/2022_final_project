package com.example.domain

import com.example.domain.dto.ChatModel
import com.example.kudata.entity.ChatContent

object DtoTranslator {
    fun chatTranslator(chatList: List<ChatContent>) : List<ChatModel> {
        val list = mutableListOf<ChatModel>()
        chatList.forEach {
            list.add(ChatModel(it.uid, it.message, it.timestamp))
        }

        return list
    }
}