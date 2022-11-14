package com.example.kudata.repository.datasource.mltranslator

interface MlKitDatasource {
    fun getLanguageType(text: String, callback: (String) -> Unit)
    fun getTranslatedText(text: String, from: String, to: String, callback: (String) -> Unit)
}