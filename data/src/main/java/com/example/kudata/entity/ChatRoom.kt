package com.example.kudata.entity

data class ChatRoom(
    val users: Map<String, Boolean>? = null,
    val contents: List<ChatContent>? = null
)
