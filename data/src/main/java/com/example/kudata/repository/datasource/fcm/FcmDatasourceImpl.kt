package com.example.kudata.repository.datasource.fcm

import com.example.kudata.api.NetworkModule
import com.example.kudata.dto.FcmData
import com.example.kudata.dto.FcmNotification
import com.example.kudata.dto.FcmRequest
import com.example.kudata.dto.FcmResponse
import com.example.kudata.utils.FCM_SERVER_KEY

class FcmDatasourceImpl: FcmDatasource {
    override suspend fun getFcmResult(
        to: String,
        title: String,
        body: String,
        qid: String,
        uid: String,
        users: List<String>,
        userProfile: String,
        isPrivate: Boolean
    ): FcmResponse? {
        var usersString = ""
        users.forEach { usersString = "$usersString$it," }
        usersString = usersString.substring(0, usersString.length)
        return NetworkModule.provideFcmService().fcmMessagingService(
            "application/json",
            "key=$FCM_SERVER_KEY",
            FcmRequest(to, FcmNotification(title, body), FcmData(qid, uid, userProfile, isPrivate.toString(), usersString))
        ).body()
    }
}