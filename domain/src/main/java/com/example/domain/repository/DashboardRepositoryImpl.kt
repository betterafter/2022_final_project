package com.example.domain.repository

import com.example.kudata.entity.DashboardQuestionContent
import com.example.kudata.repository.DashboardRepository
import com.example.kudata.repository.datasource.dashboard.DashboardDatasource
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardDatasource: DashboardDatasource
) : DashboardRepository {
    override suspend fun postQuestion(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>
    ) {
        dashboardDatasource.postQuestion(uid, title, text, timestamp, imageList)
    }

    override suspend fun postAnswer(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>
    ) {

    }

    override suspend fun getQuestions(uid: String?): List<DashboardQuestionContent>? {
        return dashboardDatasource.getQuestions(uid)
    }
}