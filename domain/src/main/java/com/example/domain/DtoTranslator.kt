package com.example.domain

import android.annotation.SuppressLint
import android.util.Log
import com.example.domain.dto.ChatModel
import com.example.domain.dto.ChatRoomModel
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.dto.UserModel
import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom
import com.example.kudata.entity.DashboardQuestionContent
import com.example.kudata.entity.User
import com.example.kudata.repository.DashboardRepository
import getValue
import java.text.SimpleDateFormat

object DtoTranslator {
    val languageMap = mapOf(
        "en" to "영어",
        "fr" to "프랑스어",
        "zh" to "중국어",
        "da" to "덴마크어",
        "nl" to "네덜란드어",
        "fi" to "핀란드어",
        "de" to "독일어",
        "el" to "그리스어",
        "ga" to "아일랜드어",
        "it" to "이탈리아어",
        "ja" to "일본어",
        "ko" to "한국어",
        "no" to "노르웨이어",
        "pl" to "폴란드어",
        "pt" to "포르투갈어",
        "ru" to "러시아어",
        "es" to "스페인어",
        "sv" to "스웨덴어",
        "th" to "대만어",
        "tr" to "터키어",
        "uk" to "우크라이나어",
        "vi" to "베트남어",
    )


    suspend fun userTranslator(user: User, dashboardRepository: DashboardRepository): UserModel {
        val questionMap = user.questionList
        val favoriteMap = user.favoriteList
        val questionList = mutableListOf<DashboardQuestionModel>()
        val favoriteList = mutableListOf<DashboardQuestionModel>()

        questionMap.forEach {
            dashboardRepository.getQuestion(it.key)?.let { it1 -> dashboardQuestionTranslator(it1) }
                ?.let { it2 -> questionList.add(it2) }
        }

        favoriteMap.forEach {
            dashboardRepository.getQuestion(it.key)?.let { it1 -> dashboardQuestionTranslator(it1) }
                ?.let { it2 -> favoriteList.add(it2) }
        }

        return UserModel(
            uid = user.uid,
            userName = user.userName,
            userEmail = user.userEmail,
            userRank = user.userRank,
            userXp = user.userXp,
            language = user.language,
            languageText = languageMap[user.language] ?: "존재하지 않는 언어",
            profile = user.profile,
            questionList = questionList,
            favoriteList = favoriteList
        )
    }

    suspend fun usersTranslator(
        users: List<User>,
        dashboardRepository: DashboardRepository
    ): List<UserModel> {
        val list = mutableListOf<UserModel>()
        users.forEach {
            list.add(userTranslator(it, dashboardRepository))
        }
        list.sortWith(comparator = { it1, it2 ->
            (it1.userRank ?: "bronze").getValue()
                .compareTo((it2.userRank ?: "bronze").getValue())
        })
        list.reverse()

        return list.toList()
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
            contents = chatRoom.content?.let { chatTranslator(it, null) }
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun chatTranslator(
        chatMap: Map<String, ChatContent>,
        compList: List<ChatModel>?
    ): List<ChatModel> {
        val list = mutableListOf<ChatModel>()
        chatMap.forEach {
            val element = chatMap[it.key]
            list.add(
                ChatModel(
                    uid = element!!.uid,
                    message = element.message,
                    translatedMessage = element.message,
                    timestamp = element.timestamp,
                    userName = "",
                    userProfile = "",
                    isReversed = false
                )
            )
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
            translatedTitle = question.title,
            text = question.text,
            translatedText = question.text,
            timestamp = question.timestamp,
            likeCount = question.likeCount,
            location = question.location,
            translatedLocation = question.location,
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
            list.add(
                DashboardQuestionModel(
                    id = it.id,
                    uid = it.uid,
                    userName = it.userName,
                    title = it.title,
                    translatedTitle = it.title,
                    text = it.text,
                    translatedText = it.text,
                    timestamp = it.timestamp,
                    likeCount = it.likeCount,
                    location = it.location,
                    translatedLocation = it.location,
                    questionState = it.questionState,
                    isPrivate = it.private,
                    answerList = it.answerList,
                    imageList = it.imageList,
                    commentList = it.commentList,
                    false
                )
            )
        }

        return list.toList()
    }
}