package com.example.kudata.repository.datasource.dashboard

import com.example.kudata.entity.DashboardAnswerContent
import com.example.kudata.entity.DashboardQuestionContent
import com.example.kudata.utils.DASHBOARD_KEY
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DashboardDatasourceImpl : DashboardDatasource {
    private val _auth = Firebase.auth
    private val db = FirebaseDatabase.getInstance()

    override suspend fun postQuestion(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>,
    ) {
        val content = DashboardQuestionContent(
            uid + timestamp, uid, title, text, timestamp, imageList, listOf(), listOf()
        )

        db.reference.child(DASHBOARD_KEY).push().setValue(content)
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
}