package com.example.domain.dto

data class ChatRoomModel(
    val qid: String = "",
    val users: Map<String, Boolean>? = null,
    val isPrivate: Boolean = false,
    val contents: List<ChatModel>? = null
)
