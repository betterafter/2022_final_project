package com.example.kudata.entity

data class ChatUser(
    private val userName: String,
    private val profileImgUrl: String,
    private val uid: String,
    private val pushToken: String,
)