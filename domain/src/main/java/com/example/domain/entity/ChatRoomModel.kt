package com.example.domain.entity

data class ChatRoomModel(
    val qid: String = "",
    val users: Map<String, Boolean>? = null,
    val end: Boolean = false,
    val isPrivate: Boolean = false,
    val contents: List<ChatModel>? = null
)
