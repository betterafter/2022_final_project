package com.example.domain.entity

import android.net.Uri

data class PostDashboardQuestionModel(
    var title: String = "",
    var details: String = "",
    var imageList: List<Uri>
)
