package com.example.domain.usecase.dashboard

import android.annotation.SuppressLint
import android.net.Uri
import com.example.domain.DtoTranslator
import com.example.domain.dto.DashboardQuestionModel
import com.example.kudata.entity.DashboardQuestionContent
import com.example.kudata.repository.DashboardRepository
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*


class DashboardUsecaseImpl @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : DashboardUsecase {
    @SuppressLint("SimpleDateFormat")
    override suspend fun postQuestion(
        title: String,
        text: String,
        imageList: List<Uri>
    ) {
        dashboardRepository.postQuestion(title, text, imageList)
    }

    @SuppressLint("SimpleDateFormat")
    override suspend fun postQuestion(
        title: String,
        text: String,
        imageList: List<Uri>,
        callback: () -> Unit
    ) {
        dashboardRepository.postQuestion(title, text, imageList, callback)
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

    override suspend fun getQuestionsInRealtime(callback: ((List<DashboardQuestionModel>?) -> Unit)) {
        dashboardRepository.getQuestionsInRealtime {
            it?.let {
                val list = DtoTranslator.dashboardQuestionTranslator(it)
                callback(list)
            }
        }
    }
}