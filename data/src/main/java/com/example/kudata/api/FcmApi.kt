package com.example.kudata.api

import com.example.kudata.entity.FcmRequest
import com.example.kudata.entity.FcmResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmApi {
    @POST("fcm/send")
    suspend fun fcmMessagingService(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Body fcmRequest: FcmRequest
    ) : Response<FcmResponse>
}