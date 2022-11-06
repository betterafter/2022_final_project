package com.example.domain.usecase.user

import android.net.Uri
import com.example.domain.DtoTranslator
import com.example.domain.dto.UserModel
import com.example.kudata.repository.UserRepository
import javax.inject.Inject

class UserUsecaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : UserUsecase {
    override suspend fun initUser() {
        userRepository.initUserInfo()
    }

    override suspend fun getUser(callback: (UserModel) -> Unit) {
        userRepository.getUser {
            callback(DtoTranslator.userTranslator(it))
        }
    }

    override suspend fun updateLanguage(lang: String) {
        userRepository.updateUserInfo(
            userUid = null,
            userName = null,
            userEmail = null,
            userRank = null,
            userXp = null,
            language = lang,
            profile = null
        )
    }

    suspend fun achieveXp(xp: Int) {

    }

    override suspend fun setUserProfileImage(uri: Uri) {
        userRepository.updateUserInfo(
            userUid = null,
            userName = null,
            userEmail = null,
            userRank = null,
            userXp = null,
            language = null,
            profile = uri
        )
    }

    suspend fun setUserName() {

    }
}