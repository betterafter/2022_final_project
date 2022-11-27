package com.example.kudata.repository.datasource.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.DashboardAnswerContent
import com.example.kudata.entity.DashboardQuestionContent
import com.example.kudata.repository.datasource.chat.ChatDataSourceImpl
import com.example.kudata.repository.datasource.user.UserDatasourceImpl
import com.example.kudata.utils.DASHBOARD_KEY
import com.example.kudata.utils.IMAGE_STORE_KEY
import com.example.kudata.utils.QuestionState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class DashboardDatasourceImpl : DashboardDatasource {
    private val _auth = Firebase.auth
    private val db = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val chatDataSource = ChatDataSourceImpl()
    private val userDataSource = UserDatasourceImpl()

    @SuppressLint("SimpleDateFormat")
    override suspend fun postQuestion(
        title: String, text: String, location: String, isPrivate: Boolean, imageList: List<Uri>, callback: (() -> Unit)?
    ) {
        _auth.currentUser?.let {
            val uid = it.uid
            val userName = it.displayName ?: kotlin.run { "" }

            CoroutineScope(Dispatchers.IO).async {
                val timestamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
                val list = mutableListOf<String>()

                imageList.map {
                    async(Dispatchers.IO) {
                        contentUpload(title, it) { name ->
                            list.add(name)
                        }
                    }
                }.awaitAll()

                val content = DashboardQuestionContent(
                    id = uid + timestamp,
                    uid = uid,
                    userName = userName,
                    title = title,
                    text = text,
                    timestamp = timestamp,
                    likeCount = "0",
                    location = location,
                    private = isPrivate,
                    questionState = QuestionState.NEW.value,
                    answerList = listOf(),
                    imageList = list,
                    commentList = listOf(),
                )

                // 공개 질문의 경우 포스팅하자마자 채팅방 바로 생성
                if (!content.private) {
                    chatDataSource.initPublicChatRoom(
                        qid = content.id, isPrivate = content.private
                    )
                }

                userDataSource.getUserInfo(null).apply {
                    val questionList = this?.questionList?.toMutableMap()
                    questionList?.set(content.id, true)

                    userDataSource.updateUserInfo(
                        null, null, null, null,
                        null, null, null, questionList, null
                    )
                }

                db.reference.child(DASHBOARD_KEY).push().setValue(content).await()
                if (callback != null) {
                    callback()
                }
            }
        }
    }

    override suspend fun postAnswer(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>,
    ) {

    }

    override suspend fun getQuestion(uid: String): DashboardQuestionContent? {
        val ref = db.reference.child(DASHBOARD_KEY).get().await()
        ref.children.forEach {
            val q = it.getValue(DashboardQuestionContent::class.java)
            if (q?.id == uid) {
                Log.d("[keykat]" ," qid: ${q?.id}, uid:: $uid")
                return it.getValue(DashboardQuestionContent::class.java)
            }
        }

        return null
    }

    override suspend fun updateQuestion(
        id: String?,
        uid: String?,
        title: String?,
        text: String?,
        likeCount: String?,
        location: String?,
        private: Boolean?,
        questionState: String?,
        answerList: List<DashboardQuestionContent>?,
        imageList: List<DashboardQuestionContent>?,
        commentList: List<DashboardQuestionContent>?,
    ) {
        val r = db.reference.child(DASHBOARD_KEY).get().await()
        r.children.forEach {
            val q = it.getValue(DashboardQuestionContent::class.java)
            if (q?.id == id) {
                val key = it.key
                key?.let {
                    val ref = db.reference.child(DASHBOARD_KEY).child(key)
                    title?.let { ref.child("title").setValue(title) }
                    text?.let { ref.child("text").setValue(text) }
                    likeCount?.let { ref.child("likeCount").setValue(likeCount) }
                    location?.let { ref.child("location").setValue(location) }
                    private?.let { ref.child("private").setValue(private) }
                    questionState?.let { ref.child("questionState").setValue(questionState) }
                    imageList?.let { ref.child("imageList").setValue(imageList) }
                    commentList?.let { ref.child("commentList").setValue(commentList) }
                    answerList?.let { ref.child("answerList").setValue(answerList) }

                    return
                }
            }
        }
    }
//
//    override suspend fun updateQuestion(
//        id: String?,
//        uid: String?,
//        title: String?,
//        text: String?,
//        likeCount: String?,
//        location: String?,
//        private: Boolean?,
//        questionState: String?,
//        answerList: List<DashboardQuestionContent>?,
//        imageList: List<DashboardQuestionContent>?,
//        commentList: List<DashboardQuestionContent>?,
//    ) {
//        _auth.currentUser?.uid?.let { currId ->
//            val r = db.reference.child(DASHBOARD_KEY).get().await()
//            r.children.forEach {
//                val q = it.getValue(DashboardQuestionContent::class.java)
//                if (q?.uid == currId) {
//                    val key = it.key
//                    key?.let {
//                        val ref = db.reference.child(DASHBOARD_KEY).child(key)
//                        title?.let { ref.child("title").setValue(title) }
//                        text?.let { ref.child("text").setValue(text) }
//                        likeCount?.let { ref.child("likeCount").setValue(likeCount) }
//                        location?.let { ref.child("location").setValue(location) }
//                        private?.let { ref.child("private").setValue(private) }
//                        questionState?.let { ref.child("questionState").setValue(questionState) }
//                        imageList?.let { ref.child("imageList").setValue(imageList) }
//                        commentList?.let { ref.child("commentList").setValue(commentList) }
//                        answerList?.let { ref.child("answerList").setValue(answerList) }
//
//                        return
//                    }
//                }
//            }
//
//        }
//    }

    override suspend fun getQuestions(uid: String?): List<DashboardQuestionContent>? {
        val list = mutableListOf<DashboardQuestionContent>()
        val ref = db.reference.child(DASHBOARD_KEY).get()
        ref.result.children.forEach {
            if (uid != null && it.key?.contains(uid) == true) {
                it.getValue(DashboardQuestionContent::class.java)?.let { it1 -> list.add(it1) }
            } else if (uid == null) {
                it.getValue(DashboardQuestionContent::class.java)?.let { it1 -> list.add(it1) }
            }
        }

        return list
    }

    override suspend fun getQuestionsInRealtime(
        callback: ((List<DashboardQuestionContent>?, List<DashboardQuestionContent>?) -> Unit)
    ) {
        if (_auth.currentUser != null) {
            val ref = db.reference.child(DASHBOARD_KEY)
            ref.orderByChild("dashboards/").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<DashboardQuestionContent>()
                    val publicList = mutableListOf<DashboardQuestionContent>()
                    snapshot.children.forEach {
                        it.getValue(DashboardQuestionContent::class.java)?.let { content ->
                            if (content.private) {
                                list.add(content)
                            } else {
                                publicList.add(content)
                            }
                        }
                    }

                    callback(list, publicList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("[keykat]", "canceled update dashboard: $error")
                }

            })

        }
    }

    @SuppressLint("SimpleDateFormat")
    private suspend fun contentUpload(
        uid: String,
        uri: Uri,
        onUploadSuccess: (String) -> Unit,
    ) {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmssSSSS").format(Date())
        val imageFileName = "IMAGE_${uid}_${timestamp}.png"
        val storageRef = storage.reference.child(IMAGE_STORE_KEY).child(imageFileName)

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
    }
}