package com.example.domain.usecase.dashboard

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
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
        location: String,
        isPrivate: Boolean,
        imageList: List<Uri>
    ) {
        dashboardRepository.postQuestion(title, text, location, isPrivate, imageList)
    }

    @SuppressLint("SimpleDateFormat")
    override suspend fun postQuestion(
        title: String,
        text: String,
        location: String,
        isPrivate: Boolean,
        imageList: List<Uri>,
        callback: () -> Unit
    ) {
        dashboardRepository.postQuestion(title, text, location, isPrivate, imageList, callback)
    }

    override suspend fun postAnswer(
        uid: String,
        title: String,
        text: String,
        timestamp: String,
        imageList: List<String>
    ) {

    }

    override suspend fun getQuestion(uid: String): DashboardQuestionModel? {
        dashboardRepository.getQuestion(uid)?.let {
            return DtoTranslator.dashboardQuestionTranslator(it)
        }
        return null
    }

    override suspend fun getAllQuestions(): List<DashboardQuestionContent>? {
        return dashboardRepository.getQuestions(null)
    }

    override suspend fun getUserQuestions(uid: String?): List<DashboardQuestionContent>? {
        return dashboardRepository.getQuestions(uid)
    }

    override suspend fun updateQuestionState(qid: String, questionState: String) {
        dashboardRepository.updateQuestion(
            qid, null, null, null, null, null,
            null, questionState, null, null, null,
        )
    }

    override suspend fun getQuestionsInRealtime(
        compList: List<DashboardQuestionModel>?,
        compList2: List<DashboardQuestionModel>?,
        callback: ((List<DashboardQuestionModel>?) -> Unit),
        callback2: ((List<DashboardQuestionModel>?) -> Unit)
    ) {
        dashboardRepository.getQuestionsInRealtime { list1, list2 ->
            list1?.let {
                val list = DtoTranslator.dashboardQuestionTranslator(it, compList)
                callback(list)
            }

            list2?.let {
                val list = DtoTranslator.dashboardQuestionTranslator(it, compList2)
                callback2(list)
            }
        }
    }
}