package com.example.kudata.repository.datasource.papago

import com.example.kudata.entity.TranslateResult

interface PapagoDatasource {
    suspend fun getPapagoTranslateResult(text: String, source: String, target: String): TranslateResult?
    suspend fun getPapagoLangResult(query: String): String?
}