package com.example.domain.usecase.user

import android.net.Uri
import com.example.domain.DtoTranslator
import com.example.domain.entity.UserModel
import com.example.kudata.repository.DashboardRepository
import com.example.kudata.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserUsecaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val dashboardRepository: DashboardRepository
) : UserUsecase {
    override suspend fun initUser() {
        userRepository.initUserInfo()
    }

    override suspend fun getUser(uid: String?, callback: (UserModel) -> Unit) {
        userRepository.getUser(uid) {
            CoroutineScope(Dispatchers.Main).launch {
                callback(DtoTranslator.userTranslator(it, dashboardRepository))
            }
        }
    }

    override suspend fun getUser(uid: String?): UserModel? {
        userRepository.getUser(uid)?.let {
            return DtoTranslator.userTranslator(it, dashboardRepository)
        } ?: run {
            return null
        }
    }

    override suspend fun getUsers(): List<UserModel> {
        return DtoTranslator.usersTranslator(userRepository.getUsers(), dashboardRepository)
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

    override suspend fun updateXp(uid: String, xp: Int) {
        userRepository.updateUserInfo(
            userUid = uid,
            userName = null,
            userEmail = null,
            userRank = null,
            userXp = xp,
            language = null,
            profile = null,
            questionList = null,
            favoriteList = null
        )
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