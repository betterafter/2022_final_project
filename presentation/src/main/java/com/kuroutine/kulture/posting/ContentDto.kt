package com.kuroutine.kulture.posting

import com.google.firebase.storage.FirebaseStorage

data class ContentDto(
    var explain: String?= null,
    var imageUrl: String?= null,
    var uid: String?= null,
    var userid: String?= null,
    var timestamp: Long?= null,
    var title: String?= null,
    var details: String?= null,
    var imageStorage: String?= null
)
