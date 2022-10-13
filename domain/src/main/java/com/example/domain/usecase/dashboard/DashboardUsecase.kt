package com.example.domain.usecase.dashboard

import com.example.kudata.entity.DashboardQuestionContent

interface DashboardUsecase {
    suspend fun postQuestion(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>
    )

    suspend fun postAnswer(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>
    )

    suspend fun getAllQuestions(): List<DashboardQuestionContent>?

    suspend fun getUserQuestions(uid: String?): List<DashboardQuestionContent>?
}