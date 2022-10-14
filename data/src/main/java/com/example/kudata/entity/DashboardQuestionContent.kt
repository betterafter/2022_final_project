package com.example.kudata.entity

data class DashboardQuestionContent(
    val id: String,
    val uid: String,
    val title: String,
    val text: String,
    val timestamp: String,
    val imageList: List<String>,
    val answerList: List<DashboardAnswerContent>,
    val commentList: List<String>
)
