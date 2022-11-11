package com.example.domain

import android.annotation.SuppressLint
import com.example.domain.dto.ChatModel
import com.example.domain.dto.ChatRoomModel
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.dto.UserModel
import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom
import com.example.kudata.entity.DashboardQuestionContent
import com.example.kudata.entity.User
import java.text.SimpleDateFormat

object DtoTranslator {
    val languageMap = mapOf(
        ("ko" to "한국어"),
        ("ja" to "일본어"),
        ("zh-CN" to "중국어 (간체)"),
        ("zh-TW" to "중국어 (번체)"),
        ("hi" to "힌디어"),
        ("en" to "영어"),
        ("es" to "스페인어"),
        ("fr" to "프랑스어"),
        ("de" to "독일어"),
        ("pt" to "포르투칼어"),
        ("vi" to "베트남어"),
        ("id" to "인도네시아어"),
        ("fa" to "페르시아어"),
        ("ar" to "아랍어"),
        ("mm" to "미얀마어"),
        ("th" to "태국어"),
        ("ru" to "러시아어"),
        ("it" to "이탈리아어"),
    )


    fun userTranslator(user: User): UserModel {
        return UserModel(
            uid = user.uid,
            userName = user.userName,
            userEmail = user.userEmail,
            userRank = user.userRank,
            userXp = user.userXp,
            language = user.language,
            languageText = languageMap[user.language] ?: "존재하지 않는 언어",
            profile = user.profile
        )
    }

    fun chatRoomsTranslator(chatRooms: List<ChatRoom>): List<ChatRoomModel> {
        val list = mutableListOf<ChatRoomModel>()
        chatRooms.forEach {
            val chatRoom = chatRoomTranslator(it)
            if (chatRoom.contents != null && chatRoom.contents.isNotEmpty()) {
                list.add(chatRoomTranslator(it))
            }
        }
        return list
    }

    private fun chatRoomTranslator(chatRoom: ChatRoom): ChatRoomModel {
        return ChatRoomModel(
            qid = chatRoom.qid,
            users = chatRoom.users,
            isPrivate = chatRoom.private,
            contents = chatRoom.content?.let { chatTranslator(it) }
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun chatTranslator(chatMap: Map<String, ChatContent>): List<ChatModel> {
        val list = mutableListOf<ChatModel>()
        chatMap.forEach {
            val element = chatMap[it.key]

            list.add(ChatModel(uid = element!!.uid, message = element.message, timestamp = element.timestamp))
        }

        // 시간 순으로 정렬 (map이라 키값으로 자동 정렬됨)
        list.sortBy { it.timestamp as Long }

        list.forEach {
            val sdf = SimpleDateFormat("MM/dd hh:mm")
            val getTime = sdf.format(it.timestamp)
            it.timestamp = getTime
        }

        return list
    }

    fun dashboardQuestionTranslator(question: DashboardQuestionContent): DashboardQuestionModel {
        return DashboardQuestionModel(
            id = question.id,
            uid = question.uid,
            userName = question.userName,
            title = question.title,
            text = question.text,
            timestamp = question.timestamp,
            likeCount = question.likeCount,
            location = question.location,
            questionState = question.questionState,
            isPrivate = question.private,
            answerList = question.answerList,
            imageList = question.imageList,
            commentList = question.commentList,
            translatedState = false,
        )
    }

    fun dashboardQuestionTranslator(
        questionList: List<DashboardQuestionContent>,
        compList: List<DashboardQuestionModel>?,
    ): List<DashboardQuestionModel> {
        val list = mutableListOf<DashboardQuestionModel>()
        questionList.forEachIndexed { index, it ->
            var isChk = false
            //Log.d("[keykat]", "compList:::: $compList")
            compList?.let { model ->
                for (idx in compList.indices) {
                    if (compList[idx].uid == it.uid && compList[idx].timestamp == it.timestamp && compList[idx].translatedState) {
                        isChk = true
                        list.add(compList[idx])
                        break
                    }
                }
            }

            if (!isChk) {
                list.add(
                    DashboardQuestionModel(
                        it.id,
                        it.uid,
                        it.userName,
                        it.title,
                        it.text,
                        it.timestamp,
                        it.likeCount,
                        it.location,
                        it.questionState,
                        it.private,
                        it.answerList,
                        it.imageList,
                        it.commentList,
                        false
                    )
                )
            }
        }

        return list.toList()
    }
}