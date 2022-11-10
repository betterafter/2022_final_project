package com.example.kudata.entity

data class ChatRoom(
    val qid: String = "",
    val private: Boolean = true,
    val users: Map<String, Boolean>? = null,
    val content: Map<String, ChatContent>? = null
)
