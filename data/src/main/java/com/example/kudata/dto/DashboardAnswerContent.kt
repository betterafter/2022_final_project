package com.example.kudata.dto

data class DashboardAnswerContent(
    val id: String? = "",
    val uid: String? = "",
    val title: String? = "",
    val text: String? = "",
    val timestamp: String = "",
    val imageList: List<String>? = null,
    val commentList: List<String>? = null
)