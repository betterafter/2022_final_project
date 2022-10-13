package com.example.kudata.repository

import com.example.kudata.entity.DashboardQuestionContent

interface DashboardRepository {
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
}