package com.example.kudata.repository

interface MlKitRepository {
    fun getLanguageType(text: String, callback: (String) -> Unit)
    fun getTranslatedText(text: String, from: String, to: String, callback: (String) -> Unit)
}