package com.example.kudata.repository.datasource.dashboard

import android.net.Uri
import com.example.kudata.entity.DashboardQuestionContent

interface DashboardDatasource {
    suspend fun postQuestion(
        title: String,
        text: String,
        timestamp: String,
        imageList: List<Uri>,
    )

    suspend fun postAnswer(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>,
    )

    suspend fun getQuestions(uid: String?): List<DashboardQuestionContent>?

    suspend fun getQuestionsInRealtime(uid: String?, callback: ((List<DashboardQuestionContent>?) -> Unit))
}