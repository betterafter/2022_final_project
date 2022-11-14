package com.example.domain.usecase.papago

import com.example.kudata.repository.MlKitRepository
import com.example.kudata.repository.PapagoRepository
import javax.inject.Inject

class TranslateUsecaseImpl @Inject constructor(
    private val papagoRepository: PapagoRepository,
    private val mlKitRepository: MlKitRepository
) : TranslateUsecase {
//    override suspend fun getText(text: String, source: String, target: String): String? =
//        papagoRepository.getPapagoResult(text = text, source = source, target = target)?.translatedText
//
//    override suspend fun getSrcLangType(text: String, source: String, target: String): String? =
//        papagoRepository.getPapagoResult(text = text, source = source, target = target)?.srcLangType
//
//    override suspend fun getTarLangType(text: String, source: String, target: String): String? =
//        papagoRepository.getPapagoResult(text = text, source = source, target = target)?.tarLangType
//
//    override suspend fun getLangCode(query: String): String?
//    = papagoRepository.getDetectLang(query = query)
//
    override suspend fun getLangCode(text: String, callback: (String) -> Unit) {
        mlKitRepository.getLanguageType(text, callback)
    }

    override suspend fun getText(text: String, source: String, target: String, callback: (String) -> Unit) {
        mlKitRepository.getTranslatedText(text, source, target, callback)
    }
}