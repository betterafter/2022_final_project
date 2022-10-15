package com.example.kudata.repository.datasource.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kudata.entity.DashboardAnswerContent
import com.example.kudata.entity.DashboardQuestionContent
import com.example.kudata.utils.DASHBOARD_KEY
import com.example.kudata.utils.IMAGE_STORE_KEY
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class DashboardDatasourceImpl : DashboardDatasource {
    private val _auth = Firebase.auth
    private val db = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()

    @SuppressLint("SimpleDateFormat")
    override suspend fun postQuestion(
        title: String,
        text: String,
        imageList: List<Uri>,
    ) {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val list = mutableListOf<String>()
        imageList.forEach {
            contentUpload(title, it) { name ->
                list.add(name)
            }
        }

        val uid = _auth.currentUser?.uid
        uid?.let {
            val content = DashboardQuestionContent(
                uid + timestamp,
                uid,
                title,
                text,
                timestamp,
                listOf(),
                list,
                listOf(),
            )

            db.reference.child(DASHBOARD_KEY).push().setValue(content)
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

    override suspend fun getQuestionsInRealtime(uid: String?, callback: ((List<DashboardQuestionContent>?) -> Unit)) {
        if (uid != null) {
            val ref = db.reference.child(DASHBOARD_KEY)
            if (ref.key?.contains(uid) == true) {
                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        TODO("Not yet implemented")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        } else {

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

        task.addOnSuccessListener {
            Log.d("[keykat]", "image uploaded")
        }

        onUploadSuccess(imageFileName)
    }
}