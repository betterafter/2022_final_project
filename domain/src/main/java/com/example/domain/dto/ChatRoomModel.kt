package com.example.domain.dto

data class ChatRoomModel(
    val qid: String = "",
    val users: Map<String, Boolean>? = null,
    val contents: List<ChatModel>? = null
)
