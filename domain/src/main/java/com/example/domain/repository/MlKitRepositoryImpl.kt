package com.example.domain.repository

import com.example.kudata.repository.MlKitRepository
import com.example.kudata.repository.datasource.mltranslator.MlKitDatasource
import javax.inject.Inject


class MlKitRepositoryImpl @Inject constructor(
    private val mlKitDatasource: MlKitDatasource
): MlKitRepository {
    override fun getLanguageType(text: String, callback: (String) -> Unit) {
        mlKitDatasource.getLanguageType(text, callback)
    }

    override fun getTranslatedText(text: String, from: String, to: String, callback: (String) -> Unit) {
        mlKitDatasource.getTranslatedText(text, from, to, callback)
    }
}