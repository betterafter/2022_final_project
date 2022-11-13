package com.kuroutine.kulture.ranking

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.user.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val userUsecase: UserUsecase
): ViewModel() {

    suspend fun getUsers() {
        userUsecase.getUsers()
    }
}