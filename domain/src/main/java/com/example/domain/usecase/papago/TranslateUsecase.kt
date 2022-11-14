package com.example.domain.usecase.papago

interface TranslateUsecase {
    //   suspend fun getText(text: String, source: String, target: String): String?
    suspend fun getText(text: String, source: String, target: String, callback: (String) -> Unit)

    //    suspend fun getSrcLangType(text: String, source: String, target: String): String?
//    suspend fun getTarLangType(text: String, source: String, target: String): String?
//    suspend fun getLangCode(query: String): String?
    fun prepare(callback: () -> Unit)
    suspend fun getLangCode(text: String, callback: (String) -> Unit)
}