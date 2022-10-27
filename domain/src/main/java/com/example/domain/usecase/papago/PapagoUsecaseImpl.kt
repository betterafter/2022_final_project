package com.example.domain.usecase.papago

import com.example.kudata.repository.PapagoRepository
import javax.inject.Inject

class PapagoUsecaseImpl @Inject constructor(
    private val papagoRepository: PapagoRepository,
) : PapagoUsecase {
    override suspend fun getText(text: String, source: String, target: String): String? =
        papagoRepository.getPapagoResult(text = text, source = source, target = target)?.translatedText

    override suspend fun getSrcLangType(text: String, source: String, target: String): String? =
        papagoRepository.getPapagoResult(text = text, source = source, target = target)?.srcLangType

    override suspend fun getTarLangType(text: String, source: String, target: String): String? =
        papagoRepository.getPapagoResult(text = text, source = source, target = target)?.tarLangType

    override suspend fun getLangCode(query: String): String?
    = papagoRepository.getDetectLang(query = query)
}