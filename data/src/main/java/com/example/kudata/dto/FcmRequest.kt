package com.example.kudata.dto

data class FcmRequest(
    var to: String?,
    var notification: FcmNotification?,
    var data: FcmData?
)