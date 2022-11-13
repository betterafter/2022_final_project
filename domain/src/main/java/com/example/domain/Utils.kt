

enum class RankType(val value: Int, val value2: String) {
    Diamond(5, "diamond"),
    Platinum(4, "platinum"),
    Gold(3, "gold"),
    Silver(2, "silver"),
    Bronze(1, "bronze");
}

fun String.getValue(): Int {
        return when(this) {
            "diamond" -> RankType.Diamond.value
            "platinum" -> RankType.Platinum.value
            "gold" -> RankType.Gold.value
            "silver" -> RankType.Silver.value
            "bronze" -> RankType.Bronze.value
            else -> { RankType.Bronze.value }
        }
}