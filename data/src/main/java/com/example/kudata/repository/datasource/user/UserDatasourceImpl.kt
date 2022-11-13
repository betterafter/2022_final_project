package com.example.kudata.repository.datasource.user

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.example.kudata.entity.User
import com.example.kudata.repository.datasource.chat.ChatDataSourceImpl
import com.example.kudata.utils.PROFILE_IMAGE_STORE_KEY
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class UserDatasourceImpl : UserDatasource {
    private val _auth = Firebase.auth
    private val _fireStore = FirebaseFirestore.getInstance()
    private val chatDataSource = ChatDataSourceImpl()
    private val storage = FirebaseStorage.getInstance()

    // 데이터베이스 체크해서 기존에 가입한 사람이면 넘김. 그렇지 않으면 초기화
    override suspend fun initUserInfo() {
        _auth.currentUser?.uid?.let { id ->
            _fireStore.collection(id).get().addOnSuccessListener {
                val user = User(
                    id,
                    _auth.currentUser?.displayName,
                    _auth.currentUser?.email,
                    "bronze",
                    0,
                    language = "en",
                    profile = "",
                )
                if (it.isEmpty) {
                    _fireStore.collection("/users").document(id).set(user)
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
        language: String?,
        profile: Uri?,
    ) {
        var profileString = ""
        profile?.let {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                _auth.currentUser?.uid?.let { it1 ->
                    contentUpload(it1, profile) { name ->
                        profileString = name
                    }
                }
            }
        }

        _auth.currentUser?.uid?.let { it ->
            val map = mutableMapOf<String, Any>()
            userName?.let { map["userName"] = it }
            userEmail?.let { map["userEmail"] = it }
            userRank?.let { map["userRank"] = it }
            userXp?.let { map["userXp"] = it }
            language?.let { map["language"] = it }
            profile?.let { map["profile"] = profileString }

            _fireStore.collection("/users").document(it).update(map)
        }
    }

    override suspend fun getUsersInfo() {
        val list = mutableListOf<User>()
        _fireStore.collection("/users").document().get().addOnCompleteListener {
            Log.d("[keykat]" ,"doc:::: $it")
        }
    }

    override suspend fun getUserInfo(uid: String?, callback: (User) -> Unit) {
        uid?.let {
            _fireStore.collection("/users").document(it).get().addOnCompleteListener { snapShot ->
                snapShot.result.data?.let { data ->
                    val user = User(
                        uid = data["uid"] as String?,
                        userName = data["userName"] as String?,
                        userEmail = data["userEmail"] as String?,
                        userRank = data["userRank"] as String?,
                        userXp = (data["userXp"] ?: 0) as Long,
                        language = (data["language"] ?: "ko") as String,
                        profile = (data["profile"] ?: "") as String
                    )

                    callback(user)
                }
            }
        } ?: run {
            _auth.currentUser?.uid?.let { it ->
                _fireStore.collection("/users").document(it).get().addOnCompleteListener { snapShot ->
                    snapShot.result.data?.let { data ->
                        val user = User(
                            uid = data["uid"] as String?,
                            userName = data["userName"] as String?,
                            userEmail = data["userEmail"] as String?,
                            userRank = data["userRank"] as String?,
                            userXp = (data["userXp"] ?: 0) as Long,
                            language = (data["language"] ?: "ko") as String,
                            profile = (data["profile"] ?: "") as String
                        )

                        callback(user)
                    }
                }
            }
        }
    }

    override suspend fun getUserInfo(uid: String?): User? {
        var user: User? = null
        uid?.let {
            _fireStore.collection("/users").document(it).get().addOnCompleteListener { snapShot ->
                snapShot.result.data?.let { data ->
                    user = User(
                        uid = data["uid"] as String?,
                        userName = data["userName"] as String?,
                        userEmail = data["userEmail"] as String?,
                        userRank = data["userRank"] as String?,
                        userXp = (data["userXp"] ?: 0) as Long,
                        language = (data["language"] ?: "ko") as String,
                        profile = (data["profile"] ?: "") as String
                    )
                }
            }.await()
        } ?: run {
            _auth.currentUser?.uid?.let { it ->
                _fireStore.collection("/users").document(it).get().addOnCompleteListener { snapShot ->
                    snapShot.result.data?.let { data ->
                        user = User(
                            uid = data["uid"] as String?,
                            userName = data["userName"] as String?,
                            userEmail = data["userEmail"] as String?,
                            userRank = data["userRank"] as String?,
                            userXp = (data["userXp"] ?: 0) as Long,
                            language = (data["language"] ?: "ko") as String,
                            profile = (data["profile"] ?: "") as String
                        )
                    }
                }
            }
        }

        return user
    }

    private suspend fun updateRank() {

    }

    @SuppressLint("SimpleDateFormat")
    private suspend fun contentUpload(
        uid: String,
        uri: Uri,
        onUploadSuccess: (String) -> Unit,
    ) {
        try {
            val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSSS").format(Date())
            val imageFileName = "IMAGE_${uid}_${timestamp}.png"
            val storageRef = storage.reference.child(PROFILE_IMAGE_STORE_KEY).child(imageFileName)

            val task = storageRef.putFile(uri).continueWithTask {
                return@continueWithTask storageRef.downloadUrl
            }
            task.await()

            task.addOnSuccessListener {
                onUploadSuccess(task.result.toString())
            }

            task.addOnFailureListener {
                onUploadSuccess("")
            }
        } catch (e: Exception) {
            Log.d("[keykat]", "e: $e")
        }
    }
}