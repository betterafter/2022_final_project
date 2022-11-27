package com.example.domain.dto

data class ChatModel(
    val uid: String,
    var message: String,
    var translatedMessage: String,
    var userProfile: String,
    var userName: String,
    var timestamp: Any,
    var isReversed: Boolean = false
)
