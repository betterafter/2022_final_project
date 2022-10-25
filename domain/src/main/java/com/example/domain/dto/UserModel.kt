package com.example.domain.dto

data class UserModel(
    val uid: String?,
    val userName: String?,
    val userEmail: String?,
    val userRank: String?,
    val userXp: Long,
    val language: String,
)