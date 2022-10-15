package com.example.kudata.entity

data class DashboardQuestionContent(
    val id: String = "",
    val uid: String = "",
    val title: String = "",
    val text: String = "",
    val timestamp: String = "",
    val answerList: List<DashboardAnswerContent>? = null,
    val imageList: List<String>? = null,
    val commentList: List<String>? = null
)
