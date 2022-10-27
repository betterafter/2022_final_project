package com.example.kudata.repository

import com.example.kudata.entity.TranslateResult

interface PapagoRepository {
    suspend fun getPapagoResult(text: String, source: String, target: String): TranslateResult?

    suspend fun getDetectLang(query: String): String?
}