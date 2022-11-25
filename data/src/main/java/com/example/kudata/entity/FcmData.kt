package com.example.kudata.entity

data class FcmData(
    var qid: String?,
    var uid: String?,
    var userProfile: String?,
    var isPrivate: Boolean?,
    var users: List<String>?
)