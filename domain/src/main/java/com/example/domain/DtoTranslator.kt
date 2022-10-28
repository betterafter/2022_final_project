package com.example.domain

import android.util.Log
import com.example.domain.dto.ChatModel
import com.example.domain.dto.ChatRoomModel
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.dto.UserModel
import com.example.kudata.entity.ChatContent
import com.example.kudata.entity.ChatRoom
import com.example.kudata.entity.DashboardQuestionContent
import com.example.kudata.entity.User

object DtoTranslator {
    fun userTranslator(user: User): UserModel {
        return UserModel(
            uid = user.uid,
            userName = user.userName,
            userEmail = user.userEmail,
            userRank = user.userRank,
            userXp = user.userXp,
            language = user.language,
        )
    }

    fun chatRoomsTranslator(chatRooms: List<ChatRoom>): List<ChatRoomModel> {
        val list = mutableListOf<ChatRoomModel>()
        chatRooms.forEach {
            list.add(chatRoomTranslator(it))
        }
        return list
    }

    private fun chatRoomTranslator(chatRoom: ChatRoom): ChatRoomModel {
        return ChatRoomModel(
            qid = chatRoom.qid,
            users = chatRoom.users,
            contents = chatRoom.content?.let { chatTranslator(it) }
        )
    }

    fun chatTranslator(chatMap: Map<String, ChatContent>): List<ChatModel> {
        val list = mutableListOf<ChatModel>()
        chatMap.forEach {
            val element = chatMap[it.key]
            list.add(ChatModel(uid = element!!.uid, message = element.message, timestamp = element.timestamp))
        }

        // 시간 순으로 정렬 (map이라 키값으로 자동 정렬됨)
        list.sortBy { it.timestamp as Long }

        return list
    }

    fun dashboardQuestionTranslator(questionList: List<DashboardQuestionContent>): List<DashboardQuestionModel> {
        val list = mutableListOf<DashboardQuestionModel>()
        questionList.forEach {
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
                    it.answerList,
                    it.imageList,
                    it.commentList,
                )
            )
        }

        return list
    }
}