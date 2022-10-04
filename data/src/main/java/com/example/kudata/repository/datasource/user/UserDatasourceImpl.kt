package com.example.kudata.repository.datasource.user

import android.util.Log
import com.example.kudata.entity.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserDatasourceImpl : UserDatasource {
    private val _auth = Firebase.auth
    private val _firestore = FirebaseFirestore.getInstance()

    override suspend fun initUserInfo() {
        _auth.currentUser?.uid?.let { it ->
            val user = User(
                it,
                _auth.currentUser?.displayName,
                _auth.currentUser?.email,
                "newbie",
                0,
            )
            _firestore.collection(it).document().set(user)
        }
    }

    // userName, userXp, userRank만 업데이트
    override suspend fun updateUserInfo(
        userUid: String?,
        userName: String?,
        userEmail: String?,
        userRank: String?,
        userXp: Int?
    ) {
        _auth.currentUser?.uid?.let { it ->
            //var userInfo = _firestore.collection(it).document()
            _firestore.collection(it).document().update(
                mapOf(
                    "userName" to userName?.run { _auth.currentUser?.displayName },
                    "userEmail" to userEmail.run { _auth.currentUser?.email },
                    "userRank" to userRank.run { "newbie" },
                    "userXp" to userXp.run { 0 }
                )
            )
        }
    }

    suspend fun getUserInfo() {
        _auth.currentUser?.uid?.let { it ->
            _firestore.collection(it).document().get()
        }
    }

    private suspend fun updateRank() {

    }
}