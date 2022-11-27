package com.example.domain.entity

data class QuestionModel(var picture:Int, var title:String, var time:String, var isOver:Boolean)
// 질문사진,     질문제목,     질문등록시간,      질문상태(FALSE==질문중, TRUE==질문완료)
