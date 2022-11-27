package com.example.kudata.entity

data class FcmNotification(
    var title: String?,
    var body: String?,
    var click_action: String = "CHAT_ACTIVITY"
)