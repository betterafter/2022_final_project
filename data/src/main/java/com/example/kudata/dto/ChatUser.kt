package com.example.kudata.dto

data class ChatUser(
    private val userName: String,
    private val profileImgUrl: String,
    private val uid: String,
    private val pushToken: String,
)