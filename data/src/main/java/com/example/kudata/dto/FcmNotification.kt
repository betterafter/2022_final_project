package com.example.kudata.dto

data class FcmNotification(
    var title: String?,
    var body: String?,
    var click_action: String = "CHAT_ACTIVITY"
)