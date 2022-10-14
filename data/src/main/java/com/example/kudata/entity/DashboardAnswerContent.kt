package com.example.kudata.entity

data class DashboardAnswerContent(
    val id: String,
    val uid: String,
    val title: String,
    val text: String,
    val timestamp: String,
    val imageList: List<String>,
    val commentList: List<String>
)