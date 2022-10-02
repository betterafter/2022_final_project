package com.example.kuroutine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.PapagoUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val papagoUsecase: PapagoUsecase
) : ViewModel() {
    private val _papagoTranslatedText = MutableLiveData<String>().apply {
        value = "Please enter the content to be translated."
    }
    val papagoTranslatedText: LiveData<String> = _papagoTranslatedText

    private val _tarLandType = MutableLiveData<String>().apply { value = "en" }
    val tarLangType: LiveData<String> = _tarLandType

    private val _srcLangType = MutableLiveData<String>().apply { value = "ko" }
    val srcLangType: LiveData<String> = _srcLangType


    fun getTranslateResult(text: String) {
        viewModelScope.launch {
            _papagoTranslatedText.value = _srcLangType.value?.let { src ->
                _tarLandType.value?.let { tar ->
                    papagoUsecase.getText(text = text, source = src, target = tar)
                }.run {
                    papagoUsecase.getText(text = text, source = src, target = "en")
                }.run {
                    papagoUsecase.getText(text = text, source = "ko", target = "en")
                }
            }
        }
    }
}