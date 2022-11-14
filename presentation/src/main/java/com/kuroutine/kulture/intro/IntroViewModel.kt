package com.kuroutine.kulture.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.papago.TranslateUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val translateUsecase: TranslateUsecase
): ViewModel() {
    val _prepareState = MutableLiveData<Boolean?>().apply { value = null }
    var prepareState: LiveData<Boolean?> = _prepareState

    fun prepare() {
        translateUsecase.prepare {
            viewModelScope.launch {
                _prepareState.value = true
            }
        }
    }
}