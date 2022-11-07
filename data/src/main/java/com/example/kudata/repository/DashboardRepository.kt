package com.example.kudata.repository

import android.net.Uri
import com.example.kudata.entity.DashboardQuestionContent

interface DashboardRepository {
    suspend fun postQuestion(
        title: String,
        text: String,
        isPrivate: Boolean,
        imageList: List<Uri>
    )

    suspend fun postQuestion(
        title: String,
        text: String,
        isPrivate: Boolean,
        imageList: List<Uri>,
        callback: () -> Unit
    )

    suspend fun postAnswer(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>,
    )

    suspend fun getQuestions(uid: String?): List<DashboardQuestionContent>?

    suspend fun getQuestionsInRealtime(callback: ((List<DashboardQuestionContent>?) -> Unit))

    suspend fun getPublicQuestionsInRealtime(callback: ((List<DashboardQuestionContent>?) -> Unit))
}