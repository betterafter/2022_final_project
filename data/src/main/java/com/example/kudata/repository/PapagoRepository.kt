package com.example.kudata.repository

import com.example.kudata.dto.TranslateResult

interface PapagoRepository {
    suspend fun getPapagoResult(text: String, source: String, target: String): TranslateResult?

    suspend fun getDetectLang(query: String): String?
}