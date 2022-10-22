package com.kuroutine.kulture.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.usecase.dashboard.DashboardUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dashboardUsecase: DashboardUsecase
) : ViewModel() {

    private val _questionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val questionList: LiveData<List<DashboardQuestionModel>?> = _questionList

    private val _searchedQuestionList = MutableLiveData<List<DashboardQuestionModel>?>().apply {
        value = null
    }
    val searchedQuestionList: LiveData<List<DashboardQuestionModel>?> = _searchedQuestionList

    fun getQuestions() {
        viewModelScope.launch {
            dashboardUsecase.getQuestionsInRealtime {
                _questionList.value = it
            }
        }
    }

    fun updateSearchedList(title: String) {
        val list: MutableList<DashboardQuestionModel> = mutableListOf()
        _questionList.value?.forEach { model ->
            if (model.title.contains(title)) {
                list.add(model)
            }
        }
        _searchedQuestionList.value = list.toList()
        Log.d("[keykat]", "${_searchedQuestionList.value}")
    }
}