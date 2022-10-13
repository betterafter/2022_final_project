package com.example.kudata.repository.datasource.dashboard

import com.example.kudata.entity.DashboardQuestionContent

interface DashboardDatasource {
    suspend fun postQuestion(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>,
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