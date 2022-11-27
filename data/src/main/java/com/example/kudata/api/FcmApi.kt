package com.example.kudata.api

import com.example.kudata.dto.FcmRequest
import com.example.kudata.dto.FcmResponse
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