package com.kuroutine.kulture.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.dashboard.DashboardUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardUsecase: DashboardUsecase
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun postQuestion() {
        viewModelScope.launch {
            dashboardUsecase.postQuestion(
                "123", "hello", "sdlkfjsdklfsajfk", "ksljfkds", listOf()
            )
        }
    }

    fun getQuestions() {

    }
}