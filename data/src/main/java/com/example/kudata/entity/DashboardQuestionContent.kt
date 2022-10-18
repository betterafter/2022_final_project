package com.example.kudata.entity

data class DashboardQuestionContent(
    val id: String = "",
    val uid: String = "",
    var userName: String = "",
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
