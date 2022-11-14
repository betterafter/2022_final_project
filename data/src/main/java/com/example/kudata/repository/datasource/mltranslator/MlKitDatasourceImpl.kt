package com.example.kudata.repository.datasource.mltranslator

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MlKitDatasourceImpl : MlKitDatasource {
    private val languageList = listOf(
        "en", "fr", "zh", "da", "nl", "fi",
        "de", "el", "ga", "it", "ja", "ko",
        "no", "pl", "pt", "ru", "es", "sv",
        "th", "tr", "uk", "vi",
    )
    private val modelManager = RemoteModelManager.getInstance()

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

    override fun downloadModels(callback: () -> Unit) {
        var downloadedCount = 0

        languageList.forEach { lang ->
            CoroutineScope(Dispatchers.Main).launch {
                val options = TranslatorOptions
                    .Builder()
                    .setSourceLanguage(lang)
                    .setTargetLanguage(lang)
                    .build()
                val translator = Translation.getClient(options)

                var conditions = DownloadConditions.Builder().build()
                translator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        downloadedCount += 1
                    }
                    .addOnFailureListener { exception ->
                        Log.d("[keykat]", "download failed: $lang")
                    }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if (downloadedCount == languageList.size) {
                    callback()
                    return@launch
                }
            }
        }
    }

    override fun getTranslatedText(text: String, from: String, to: String, callback: (String) -> Unit) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(from)
            .setTargetLanguage(to)
            .build()
        val translator = Translation.getClient(options)

        Log.d("[keykat]", "getTRanslatedText")

        translator.translate(text)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                callback(translatedText)
                Log.d("[keykat]", "text: $text -> translateText: $translatedText")
            }
            .addOnFailureListener { exception ->
                callback(text)
            }

//        translator.downloadModelIfNeeded(DownloadConditions.Builder().build())
//            .addOnSuccessListener {
//                Log.d("[keykat]", "download model!!!")
//                translator.translate(text)
//                    .addOnSuccessListener { translatedText ->
//                        // Translation successful.
//                        callback(translatedText)
//                        Log.d("[keykat]", "text: $text -> translateText: $translatedText")
//                    }
//                    .addOnFailureListener { exception ->
//                        callback(text)
//                    }
//            }
//            .addOnFailureListener { exception ->
//                callback(text)
//            }

    }
}