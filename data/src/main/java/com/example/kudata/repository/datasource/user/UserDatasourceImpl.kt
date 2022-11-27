package com.example.kudata.repository.datasource.user

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.example.kudata.dto.User
import com.example.kudata.repository.datasource.chat.ChatDataSourceImpl
import com.example.kudata.utils.PROFILE_IMAGE_STORE_KEY
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                CoroutineScope(Dispatchers.IO).launch {
                    val token = FirebaseMessaging.getInstance().token.await()

                    val user = User(
                        uid = id,
                        messageToken = token,
                        userName = _auth.currentUser?.displayName,
                        userEmail = _auth.currentUser?.email,
                        userRank = "bronze",
                        userXp = 0,
                        language = "en",
                        profile = "",
                        mapOf(),
                        mapOf()
                    )

                    Log.d("[keykat]", "id::: $id")
                    Log.d("[keykat]", "token::: $token")
                    if (it.isEmpty) {
                        _fireStore.collection("/users").document(id).set(user)
                    } else {
                        val map = mutableMapOf<String, Any>()
                        map["messageToken"] = token
                        _fireStore.collection("/users").document(id).update(map)
                    }
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
        questionList: Map<String, Any>?,
        favoriteList: Map<String, Any>?,
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

        val user = getUserInfo(userUid)

        _auth.currentUser?.uid?.let { it ->
            val map = mutableMapOf<String, Any>()
            userName?.let { map["userName"] = it }
            userEmail?.let { map["userEmail"] = it }
            userRank?.let { map["userRank"] = it }
            userXp?.let {
                map["userXp"] = if (user != null) user.userXp + userXp else userXp
            }
            language?.let { map["language"] = it }
            profile?.let { map["profile"] = profileString }
            questionList?.let { map["questionList"] = questionList }
            favoriteList?.let { map["favoriteList"] = favoriteList }

            _fireStore.collection("/users").document(it).update(map)
        }
    }

    override suspend fun getUsersInfo(): List<User> {
        val list = mutableListOf<User>()
        val ref = _fireStore.collection("/users").get().await()
        ref.documents.forEach { doc ->
            doc.data?.let { data ->
                val questionList = getQuestionListFromData(data)
                val favoriteList = getFavoriteListsFromData(data)

                val user = User(
                    uid = data["uid"] as String?,
                    messageToken = data["messageToken"] as String?,
                    userName = data["userName"] as String?,
                    userEmail = data["userEmail"] as String?,
                    userRank = data["userRank"] as String?,
                    userXp = data["userXp"] as Long,
                    language = (data["language"] ?: "ko") as String,
                    profile = (data["profile"] ?: "") as String,
                    questionList = questionList,
                    favoriteList = favoriteList
                )
                list.add(user)
            }
        }

        return list.toList()
    }

    override suspend fun getUserInfo(uid: String?, callback: (User) -> Unit) {
        uid?.let {
            Log.d("[keykat]", "it::::: $it")
            _fireStore.collection("/users").document(it).get().addOnCompleteListener { snapShot ->
                snapShot.result.data?.let { data ->
                    val questionList = getQuestionListFromData(data)
                    val favoriteList = getFavoriteListsFromData(data)

                    val user = User(
                        uid = data["uid"] as String?,
                        messageToken = data["messageToken"] as String?,
                        userName = data["userName"] as String?,
                        userEmail = data["userEmail"] as String?,
                        userRank = data["userRank"] as String?,
                        userXp = (data["userXp"] ?: 0) as Long,
                        language = (data["language"] ?: "ko") as String,
                        profile = (data["profile"] ?: "") as String,
                        questionList = questionList,
                        favoriteList = favoriteList
                    )

                    callback(user)
                }
            }
        } ?: run {
            _auth.currentUser?.uid?.let { it ->
                _fireStore.collection("/users").document(it).get().addOnCompleteListener { snapShot ->
                    snapShot.result.data?.let { data ->
                        val questionList = getQuestionListFromData(data)
                        val favoriteList = getFavoriteListsFromData(data)

                        val user = User(
                            uid = data["uid"] as String?,
                            messageToken = data["messageToken"] as String?,
                            userName = data["userName"] as String?,
                            userEmail = data["userEmail"] as String?,
                            userRank = data["userRank"] as String?,
                            userXp = (data["userXp"] ?: 0) as Long,
                            language = (data["language"] ?: "ko") as String,
                            profile = (data["profile"] ?: "") as String,
                            questionList = questionList,
                            favoriteList = favoriteList
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
            val ref = _fireStore.collection("/users").document(it).get().await()
            ref.data?.let { data ->
                val questionList = getQuestionListFromData(data)
                val favoriteList = getFavoriteListsFromData(data)

                user = User(
                    uid = data["uid"] as String?,
                    messageToken = data["messageToken"] as String?,
                    userName = data["userName"] as String?,
                    userEmail = data["userEmail"] as String?,
                    userRank = data["userRank"] as String?,
                    userXp = (data["userXp"] ?: 0) as Long,
                    language = (data["language"] ?: "ko") as String,
                    profile = (data["profile"] ?: "") as String,
                    questionList = questionList,
                    favoriteList = favoriteList
                )
            }
        } ?: run {
            _auth.currentUser?.uid?.let { it ->
                val ref = _fireStore.collection("/users").document(it).get().await()
                ref.data?.let { data ->
                    val questionList = getQuestionListFromData(data)
                    val favoriteList = getFavoriteListsFromData(data)

                    user = User(
                        uid = data["uid"] as String?,
                        messageToken = data["messageToken"] as String?,
                        userName = data["userName"] as String?,
                        userEmail = data["userEmail"] as String?,
                        userRank = data["userRank"] as String?,
                        userXp = (data["userXp"] ?: 0) as Long,
                        language = (data["language"] ?: "ko") as String,
                        profile = (data["profile"] ?: "") as String,
                        questionList = questionList,
                        favoriteList = favoriteList
                    )
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

    private fun getFavoriteListsFromData(data: Map<String, Any>): Map<String, Any> {
        val favoriteListData = data["favoriteList"] as Map<*, *>
        val favoriteList = mutableMapOf<String, Any>()

        favoriteListData.forEach {
            if (it.key is String) {
                favoriteList[it.key as String] = it.value as Any
            }
        }

        return favoriteList
    }

    private fun getQuestionListFromData(data: Map<String, Any>): Map<String, Any> {
        val questionListData = data["questionList"] as Map<*, *>
        val questionList = mutableMapOf<String, Any>()

        questionListData.forEach {
            if (it.key is String) {
                questionList[it.key as String] = it.value as Any
            }
        }

        return questionList
    }
}