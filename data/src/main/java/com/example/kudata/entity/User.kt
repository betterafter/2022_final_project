package com.example.kudata.entity

data class User(
    val uid: String?,
    val userName: String?,
    val userEmail: String?,
    val userRank: String?,
    val userXp: Long,
    val language: String,
    val profile: String,
)

//1. ko: 한국어
//2. ja: 일본어
//3. zh-CN: 중국어 간체
//4. zh-TW: 중국어 번체
//5. hi: 힌디어
//6. en: 영어
//7. es: 스페인어
//8. fr: 프랑스어
//9. de: 독일어
//10. pt: 포르투갈어
//11. vi: 베트남어
//12. id: 인도네시아어
//13. fa: 페르시아어
//14. ar: 아랍어
//15. mm: 미얀마어
//16. th: 태국어
//17. ru: 러시아어
//18. it: 이탈리아어
//19. unk: 알 수 없음