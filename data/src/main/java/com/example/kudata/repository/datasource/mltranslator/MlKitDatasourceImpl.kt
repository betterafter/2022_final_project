package com.example.kudata.repository.datasource.mltranslator

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MlKitDatasourceImpl : MlKitDatasource {
    private val languageIdentifier = LanguageIdentification
        .getClient(
            LanguageIdentificationOptions.Builder()
                .setConfidenceThreshold(0.34f)
                .build()
        )

    override fun getLanguageType(text: String, callback: (String) -> Unit) {
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                callback(languageCode)
            }
            .addOnFailureListener {
                callback("und")
            }
    }

    override fun getTranslatedText(text: String, from: String, to: String, callback: (String) -> Unit) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(from)
            .setTargetLanguage(to)
            .build()
        val translator = Translation.getClient(options)

        translator.downloadModelIfNeeded(DownloadConditions.Builder().build())
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        // Translation successful.
                        callback(translatedText)
                        Log.d("[keykat]", "text: $text -> translateText: $translatedText")
                    }
                    .addOnFailureListener { exception ->
                        callback(text)
                    }
            }
            .addOnFailureListener { exception ->
                callback(text)
            }

    }
}