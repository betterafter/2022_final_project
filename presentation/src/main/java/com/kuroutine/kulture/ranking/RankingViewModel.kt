package com.kuroutine.kulture.ranking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.UserModel
import com.example.domain.usecase.user.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val userUsecase: UserUsecase
): ViewModel() {
    val _userList = MutableLiveData<List<UserModel>?>().apply { value = null }
    var userList: LiveData<List<UserModel>?> = _userList

    suspend fun getUsers() {
        viewModelScope.launch {
            _userList.value = userUsecase.getUsers()
        }
    }
}