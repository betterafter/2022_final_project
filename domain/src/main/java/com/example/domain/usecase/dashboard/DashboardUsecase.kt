package com.example.domain.usecase.dashboard

import android.net.Uri
import com.example.domain.dto.DashboardQuestionModel
import com.example.kudata.entity.DashboardQuestionContent

interface DashboardUsecase {
    suspend fun postQuestion(
        title: String,
        text: String,
        imageList: List<Uri>
    )

    suspend fun postQuestion(
        title: String,
        text: String,
        imageList: List<Uri>,
        callback: () -> Unit
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

    suspend fun getQuestionsInRealtime(callback: ((List<DashboardQuestionModel>?) -> Unit))
}