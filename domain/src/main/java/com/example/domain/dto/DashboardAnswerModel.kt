package com.example.domain.dto

data class DashboardAnswerModel(
    val uid: String,
    val title: String,
    val text: String,
    val time: String,
    val imageList: List<String>,
    val commentList: List<String>
)