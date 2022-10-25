package com.example.domain

import com.example.domain.dto.ChatModel
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.dto.UserModel
import com.example.kudata.entity.ChatContent
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
            language = user.language
        )
    }

    fun chatTranslator(chatList: List<ChatContent>): List<ChatModel> {
        val list = mutableListOf<ChatModel>()
        chatList.forEach {
            list.add(ChatModel(it.uid, it.message, it.timestamp))
        }

        return list
    }

    fun dashboardQuestionTranslator(questionList: List<DashboardQuestionContent>): List<DashboardQuestionModel> {
        val list = mutableListOf<DashboardQuestionModel>()
        questionList.forEach {
            list.add(
                DashboardQuestionModel(
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