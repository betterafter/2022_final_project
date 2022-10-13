package com.example.domain.usecase.dashboard

import com.example.kudata.entity.DashboardQuestionContent
import com.example.kudata.repository.DashboardRepository
import javax.inject.Inject

class DashboardUsecaseImpl @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : DashboardUsecase {
    override suspend fun postQuestion(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>
    ) {
        dashboardRepository.postQuestion(uid, title, text, timestamp, imageList)
    }

    override suspend fun postAnswer(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>
    ) {

    }

    override suspend fun getAllQuestions(): List<DashboardQuestionContent>? {
        return dashboardRepository.getQuestions(null)
    }

    override suspend fun getUserQuestions(uid: String?): List<DashboardQuestionContent>? {
        return dashboardRepository.getQuestions(uid)
    }
}