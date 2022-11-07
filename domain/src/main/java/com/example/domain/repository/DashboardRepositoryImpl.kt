package com.example.domain.repository

import android.net.Uri
import com.example.kudata.entity.DashboardQuestionContent
import com.example.kudata.repository.DashboardRepository
import com.example.kudata.repository.datasource.dashboard.DashboardDatasource
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardDatasource: DashboardDatasource
) : DashboardRepository {
    override suspend fun postQuestion(
        title: String,
        text: String,
        isPrivate: Boolean,
        imageList: List<Uri>
    ) {
        dashboardDatasource.postQuestion(title, text, isPrivate, imageList, null)
    }

    override suspend fun postQuestion(
        title: String,
        text: String,
        isPrivate: Boolean,
        imageList: List<Uri>,
        callback: () -> Unit
    ) {
        dashboardDatasource.postQuestion(title, text, isPrivate, imageList, callback)
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

    override suspend fun getQuestionsInRealtime(
        callback: ((List<DashboardQuestionContent>?, List<DashboardQuestionContent>?) -> Unit)
    ) {
        dashboardDatasource.getQuestionsInRealtime(callback)
    }
}