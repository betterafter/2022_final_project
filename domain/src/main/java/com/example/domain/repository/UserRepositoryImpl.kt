package com.example.domain.repository

import com.example.kudata.repository.UserRepository
import com.example.kudata.repository.datasource.user.UserDatasource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDatasource: UserDatasource
) : UserRepository {
    override suspend fun initUserInfo() {
        userDatasource.initUserInfo()
    }

    override suspend fun updateUserInfo(
        userUid: String?,
        userName: String?,
        userEmail: String?,
        userRank: String?,
        userXp: Int?
    ) {
        userDatasource.updateUserInfo(userUid, userName, userEmail, userRank, userXp)
    }

//    suspend fun updateUserRef(firebaseUser: FirebaseUser) {
//
//    }
}