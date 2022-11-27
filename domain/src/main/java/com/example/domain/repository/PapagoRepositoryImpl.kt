package com.example.domain.repository

import com.example.kudata.dto.TranslateResult
import com.example.kudata.repository.PapagoRepository
import com.example.kudata.repository.datasource.papago.PapagoDatasource
import javax.inject.Inject

class PapagoRepositoryImpl @Inject constructor(
    private val papagoDatasource: PapagoDatasource,
) : PapagoRepository {
    override suspend fun getPapagoResult(text: String, source: String, target: String): TranslateResult? =
        papagoDatasource.getPapagoTranslateResult(text = text, source = source, target = target)

    override suspend fun getDetectLang(query: String): String? = papagoDatasource.getPapagoLangResult(query = query)
}