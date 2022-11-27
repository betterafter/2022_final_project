package com.example.kudata.dto

import com.google.gson.annotations.SerializedName

data class PapagoResponse(
    @SerializedName("message")
    val message: TranslateResultMessage
)
