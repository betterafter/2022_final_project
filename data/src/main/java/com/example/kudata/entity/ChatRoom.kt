package com.example.kudata.entity

data class ChatRoom(
    val users: Map<String, Boolean>,
    val contents: Map<String, ChatContent>
)
