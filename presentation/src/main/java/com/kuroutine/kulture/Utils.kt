package com.kuroutine.kulture

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

val GOOGLE_SIGNIN_RESULT_CODE: Int = 17765
val PICK_IMG_FROM_ALBUM = 0

val EXTRA_KEY_MOVETOCHAT = "moveToChat"
val EXTRA_QKEY_MOVETOCHAT = "qMoveToChat"
val EXTRA_KEY_ISPRIVATE = "isPrivate"
val EXTRA_KEY_USERS = "users"
val EXTRA_MAIN_VIEWPAGER_INDEX = "mainViewPagerIndex"
val EXTRA_SHOULD_MOVE_TO_SPECIFIC_PAGE = "shouldMoveToSpecificPage"

val PAGE_CHAT = "pageChat"

@SuppressLint("SimpleDateFormat")
fun SimpleDateFormat.now(): String =
    SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
