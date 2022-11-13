package com.kuroutine.kulture.data

enum class ChatViewType(val value: Int) {
    LEFT(0), RIGHT(1)
}

enum class RankType(val value: Int, val value2: String) {
    Diamond(5, "diamond"),
    Platinum(4, "platinum"),
    Gold(3, "gold"),
    Silver(2, "silver"),
    Bronze(1, "bronze");
}