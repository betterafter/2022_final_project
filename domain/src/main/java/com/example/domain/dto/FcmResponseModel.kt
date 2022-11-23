package com.example.domain.dto

data class FcmResponseModel(
    var multicastId: String?,
    var success: Number?,
    var failure: Number?,
    var canonicalIds: Number?,
    var results: List<FcmMessageResultModel?>?
)