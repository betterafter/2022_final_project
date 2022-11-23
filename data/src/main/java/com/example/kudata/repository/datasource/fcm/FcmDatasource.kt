package com.example.kudata.repository.datasource.fcm

import com.example.kudata.entity.FcmResponse

interface FcmDatasource {
    suspend fun getFcmResult(
        to: String,
        title: String,
        body: String,
        qid: String,
        uid: String,
        isPrivate: Boolean
    ): FcmResponse?
}