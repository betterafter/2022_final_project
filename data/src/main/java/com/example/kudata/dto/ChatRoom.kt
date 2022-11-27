package com.example.kudata.dto

data class ChatRoom(
    val qid: String = "",
    val private: Boolean = true,
    val end: Boolean = false,
    val users: Map<String, Boolean>? = null,
    val content: Map<String, ChatContent>? = null
)
