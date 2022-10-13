package com.example.domain.dto

import com.example.kudata.entity.DashboardAnswerContent

data class DashboardQuestionModel(
    val uid: String,
    val title: String,
    val text: String,
    val time: String,
    val imageList: List<String>,
    val answerList: List<DashboardAnswerModel>,
    val commentList: List<String>
)
