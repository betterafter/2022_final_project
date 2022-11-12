package com.kuroutine.kulture

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

val GOOGLE_SIGNIN_RESULT_CODE: Int = 17765
val PICK_IMG_FROM_ALBUM = 0

val EXTRA_KEY_MOVETOCHAT = "moveToChat"
val EXTRA_QKEY_MOVETOCHAT = "qMoveToChat"
val EXTRA_KEY_ISPRIVATE = "isPrivate"

@SuppressLint("SimpleDateFormat")
fun SimpleDateFormat.now(): String =
    SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
