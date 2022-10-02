package com.example.kudata.repository.datasource

import com.example.kudata.entity.TranslateResult

interface PapagoDatasource {
    suspend fun getPapagoTranslateResult(text : String, source : String, target : String): TranslateResult?
}