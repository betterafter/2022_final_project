package com.example.domain.repository

import com.example.kudata.entity.TranslateResult
import com.example.kudata.repository.PapagoRepository
import com.example.kudata.repository.datasource.PapagoDatasource
import javax.inject.Inject

class PapagoRepositoryImpl @Inject constructor(
    private val papagoDatasource: PapagoDatasource,
) : PapagoRepository {
    override suspend fun getPapagoResult(text: String, source: String, target: String): TranslateResult?
            = papagoDatasource.getPapagoTranslateResult(text = text, source = source, target = target)
}