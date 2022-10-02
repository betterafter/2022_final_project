package com.example.kudata.repository

import com.example.kudata.entity.TranslateResult
import com.example.kudata.repository.datasource.PapagoDatasource
import javax.inject.Inject

interface PapagoRepository {
    suspend fun getPapagoResult(text: String, source: String, target: String): TranslateResult?
}