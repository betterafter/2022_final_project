package com.example.kudata.entity

import com.google.gson.annotations.SerializedName

data class TranslateResult(
    @SerializedName("translatedText")
    val translatedText: String,

//    @SerializedName("srcLangType")
//    val srcLangType: String,
//
//    @SerializedName("tarLangType")
//    val tarLangType: String,
)
