package com.example.domain.usecase.user

import com.example.kudata.repository.UserRepository
import javax.inject.Inject

class UserUsecaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : UserUsecase {
    override suspend fun initUser() {
        userRepository.initUserInfo()
    }

    suspend fun achieveXp(xp: Int) {

    }

    suspend fun setUserProfileImage() {

    }

    suspend fun setUserName() {

    }
}