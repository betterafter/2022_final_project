package com.example.domain.dto

data class ChatModel(
    val uid: String,
    val message: String,
    var translatedMessage: String,
    var timestamp: Any,
)
