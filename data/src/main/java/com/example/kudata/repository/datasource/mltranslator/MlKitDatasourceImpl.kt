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
                .setConfidenceThreshold(0.3f)
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
            languageList.forEach { lang2 ->
                CoroutineScope(Dispatchers.Main).launch {
                    val options = TranslatorOptions
                        .Builder()
                        .setSourceLanguage(lang)
                        .setTargetLanguage(lang2)
                        .build()
                    val translator = Translation.getClient(options)

                    var conditions = DownloadConditions.Builder().build()
                    translator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener {
                            downloadedCount += 1
                            Log.d("[keykat]", "$downloadedCount")
                        }
                        .addOnFailureListener { exception ->
                            Log.d("[keykat]", "download failed: $lang")
                        }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if (downloadedCount == languageList.size * languageList.size) {
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

        translator.translate(text)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                callback(translatedText)
            }
            .addOnFailureListener { exception ->
                callback(text)
            }

//        translator.downloadModelIfNeeded(DownloadConditions.Builder().build())
//            .addOnSuccessListener {
//                translator.translate(text)
//                    .addOnSuccessListener { translatedText ->
//                        // Translation successful.
//                        callback(translatedText)
//                        // Log.d("[keykat]", "text: $text -> translateText: $translatedText")
//                    }
//                    .addOnFailureListener { exception ->
//                        Log.d("[keykat]", "exception: $exception")
//                        callback(text)
//                    }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("[keykat]", "exception: $exception")
//                callback(text)
//            }
    }
}