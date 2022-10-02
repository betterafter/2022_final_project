package com.example.kudata.api

import com.example.kudata.entity.PapagoRequest
import com.example.kudata.entity.PapagoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PapagoApi {
    @POST("/v1/papago/n2mt")
    suspend fun papagoService(
        @Header("X-Naver-Client-Id") Client_Id : String,
        @Header("X-Naver-Client-Secret") Client_Secret : String,
        @Body papagoRequest : PapagoRequest
    ) : Response<PapagoResponse>
}