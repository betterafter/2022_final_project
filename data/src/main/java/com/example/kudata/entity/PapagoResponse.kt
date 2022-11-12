package com.example.kudata.entity

import com.google.gson.annotations.SerializedName

data class PapagoResponse(
    @SerializedName("message")
    val message: TranslateResultMessage
)
