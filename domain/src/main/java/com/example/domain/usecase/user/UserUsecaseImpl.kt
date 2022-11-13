package com.example.domain.usecase.user

import android.net.Uri
import android.util.Log
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

    override suspend fun getUser(uid: String?, callback: (UserModel) -> Unit) {
        userRepository.getUser(uid) {
            callback(DtoTranslator.userTranslator(it))
        }
    }

    override suspend fun getUser(uid: String?): UserModel? {
        userRepository.getUser(uid)?.let {
            return DtoTranslator.userTranslator(it)
        } ?: run {
            return null
        }
    }

    override suspend fun getUsers(): List<UserModel> {
        return DtoTranslator.usersTranslator(userRepository.getUsers())
    }

    override suspend fun updateLanguage(lang: String) {
        userRepository.updateUserInfo(
            userUid = null,
            userName = null,
            userEmail = null,
            userRank = null,
            userXp = null,
            language = lang,
            profile = null,
            questionList = null,
            favoriteList = null
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
            profile = uri,
            questionList = null,
            favoriteList = null
        )
    }

    suspend fun setUserName() {

    }
}