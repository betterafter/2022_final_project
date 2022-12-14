package com.example.domain.dto

import com.example.kudata.entity.DashboardAnswerContent

data class DashboardQuestionModel(
    val id: String = "",
    val uid: String = "",
    var userName: String = "",
    var title: String = "",
    var translatedTitle: String = "",
    var text: String = "",
    var translatedText: String = "",
    val timestamp: String = "",
    val likeCount: String = "",
    var location: String = "",
    var translatedLocation: String = "",
    var questionState: String = "",
    var isPrivate: Boolean = true,
    val answerList: List<DashboardAnswerContent>? = null,
    val imageList: List<String>? = null,
    val commentList: List<String>? = null,
    var translatedState: Boolean = false,
)
