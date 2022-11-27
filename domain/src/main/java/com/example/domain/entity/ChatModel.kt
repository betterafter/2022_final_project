package com.example.domain.entity

data class ChatModel(
    val uid: String,
    var message: String,
    var translatedMessage: String,
    var userProfile: String,
    var userName: String,
    var timestamp: Any,
    var isReversed: Boolean = false
)
