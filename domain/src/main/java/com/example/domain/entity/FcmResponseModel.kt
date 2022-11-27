package com.example.domain.entity

data class FcmResponseModel(
    var multicastId: String?,
    var success: Number?,
    var failure: Number?,
    var canonicalIds: Number?,
    var results: List<FcmMessageResultModel?>?
)