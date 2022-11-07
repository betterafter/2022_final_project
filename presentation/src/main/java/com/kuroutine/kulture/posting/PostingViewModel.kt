package com.kuroutine.kulture.posting

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.dashboard.DashboardUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingViewModel @Inject constructor(
    private val dashboardUsecase: DashboardUsecase
) : ViewModel() {
    private val _imageList = MutableLiveData<List<Uri>>().apply {
        value = listOf()
    }
    val imageList: LiveData<List<Uri>> = _imageList

    fun clearImageList() {
        _imageList.value = listOf()
    }

    fun addImageList(uri: Uri) {
        _imageList.value = _imageList.value?.plus(uri)
    }

    fun imageListSize(): Int {
        if (_imageList.value == null) return 0
        return _imageList.value!!.size
    }

    fun postQuestion(title: String, content: String, isPrivate: Boolean, callback: () -> Unit) {
        viewModelScope.launch {
            dashboardUsecase.postQuestion(
                title = title,
                text = content,
                isPrivate = isPrivate,
                imageList = _imageList.value ?: listOf(),
                callback = { callback() }
            )
        }
    }
}