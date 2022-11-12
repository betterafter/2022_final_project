package com.example.kudata.repository.datasource.user

import android.util.Log
import com.example.kudata.entity.User
import com.example.kudata.repository.datasource.chat.ChatDataSourceImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserDatasourceImpl : UserDatasource {
    private val _auth = Firebase.auth
    private val _fireStore = FirebaseFirestore.getInstance()
    private val chatDataSource = ChatDataSourceImpl()

    // 데이터베이스 체크해서 기존에 가입한 사람이면 넘김. 그렇지 않으면 초기화
    override suspend fun initUserInfo() {
        _auth.currentUser?.uid?.let { id ->
            _fireStore.collection(id).get().addOnSuccessListener {
                if (it.isEmpty) {
                    val user = User(
                        id,
                        _auth.currentUser?.displayName,
                        _auth.currentUser?.email,
                        "newbie",
                        0,
                        language = "en",
                    )
                    _fireStore.collection(id).document("/user").set(user)
                }
            }
        }
    }

    // userName, userXp, userRank만 업데이트
    override suspend fun updateUserInfo(
        userUid: String?,
        userName: String?,
        userEmail: String?,
        userRank: String?,
        userXp: Int?,
        language: String?
    ) {
        _auth.currentUser?.uid?.let { it ->
            val map = mutableMapOf<String, Any>()
            userName?.let { map["userName"] = it }
            userEmail?.let { map["userEmail"] = it }
            userRank?.let { map["userRank"] = it }
            userXp?.let { map["userXp"] = it }
            language?.let { map["language"] = it }

            _fireStore.collection(it).document("/user").update(map)
        }
    }

    override suspend fun getUserInfo(callback: (User) -> Unit) {
        _auth.currentUser?.uid?.let { it ->
            _fireStore.collection(it).document("/user").get().addOnCompleteListener {
                it.result.data?.let { data ->
                    val user = User(
                        uid = data["uid"] as String?,
                        userName = data["userName"] as String?,
                        userEmail = data["userEmail"] as String?,
                        userRank = data["userRank"] as String?,
                        userXp = (data["userXp"] ?: 0) as Long,
                        language = (data["language"] ?: "ko") as String,
                    )

                    callback(user)
                }
            }
        }
    }

    private suspend fun updateRank() {

    }
}