package com.example.domain.dto

import com.example.kudata.entity.DashboardAnswerContent

data class DashboardQuestionModel(
    val uid: String = "",
    val userName: String = "",
    val title: String = "",
    val text: String = "",
    val timestamp: String = "",
    val likeCount: String = "",
    val location: String = "",
    val questionState: String = "",
    val answerList: List<DashboardAnswerContent>? = null,
    val imageList: List<String>? = null,
    val commentList: List<String>? = null
)
