package com.example.kudata.entity

data class FcmRequest(
    var to: String?,
    var notification: FcmNotification?,
    var data: FcmData?
)