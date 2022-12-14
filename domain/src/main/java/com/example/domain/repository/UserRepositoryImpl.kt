package com.example.domain.repository

import android.net.Uri
import com.example.kudata.entity.User
import com.example.kudata.repository.UserRepository
import com.example.kudata.repository.datasource.user.UserDatasource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDatasource: UserDatasource
) : UserRepository {
    override suspend fun initUserInfo() {
        userDatasource.initUserInfo()
    }

    override suspend fun getUser(uid: String?, callback: (User) -> Unit) {
        userDatasource.getUserInfo(uid, callback)
    }

    override suspend fun getUser(uid: String?): User? {
        return userDatasource.getUserInfo(uid)
    }

    override suspend fun getUsers(): List<User> {
        return userDatasource.getUsersInfo()
    }

    override suspend fun updateUserInfo(
        userUid: String?,
        userName: String?,
        userEmail: String?,
        userRank: String?,
        userXp: Int?,
        language: String?,
        profile: Uri?,
        questionList: Map<String, Any>?,
        favoriteList: Map<String, Any>?,
    ) {
        userDatasource.updateUserInfo(
            userUid,
            userName,
            userEmail,
            userRank,
            userXp,
            language,
            profile,
            questionList,
            favoriteList
        )
    }
}