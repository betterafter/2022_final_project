package com.example.kudata.repository.datasource.fcm

import com.example.kudata.api.NetworkModule
import com.example.kudata.entity.FcmData
import com.example.kudata.entity.FcmNotification
import com.example.kudata.entity.FcmRequest
import com.example.kudata.entity.FcmResponse
import com.example.kudata.utils.FCM_SERVER_KEY

class FcmDatasourceImpl: FcmDatasource {
    override suspend fun getFcmResult(
        to: String,
        title: String,
        body: String,
        qid: String,
        uid: String,
        isPrivate: Boolean
    ): FcmResponse? =
        NetworkModule.provideFcmService().fcmMessagingService(
            "application/json",
            "key=$FCM_SERVER_KEY",
            FcmRequest(to, FcmNotification(title, body), FcmData(qid, uid, isPrivate))
        ).body()
}