package com.kuroutine.kulture.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entity.LanguageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor() : ViewModel() {
    private val _languageList = MutableLiveData<List<LanguageModel>?>().apply { value = null }
    val languageList: LiveData<List<LanguageModel>?> = _languageList

    fun setLanguageList() {
        _languageList.value = listOf(
            LanguageModel("en", "영어"),
            LanguageModel("fr", "프랑스어"),
            LanguageModel("zh", "중국어"),
            LanguageModel("da", "덴마크어"),
            LanguageModel("nl", "네덜란드어"),
            LanguageModel("fi", "핀란드어"),
            LanguageModel("de", "독일어"),
            LanguageModel("el", "그리스어"),
            LanguageModel("ga", "아일랜드어"),
            LanguageModel("it", "이탈리아어"),
            LanguageModel("ja", "일본어"),
            LanguageModel("ko", "한국어"),
            LanguageModel("no", "노르웨이어"),
            LanguageModel("pl", "폴란드어"),
            LanguageModel("pt", "포르투갈어"),
            LanguageModel("ru", "러시아어"),
            LanguageModel("es", "스페인어"),
            LanguageModel("sv", "스웨덴어"),
            LanguageModel("th", "대만어"),
            LanguageModel("tr", "터키어"),
            LanguageModel("uk", "우크라이나어"),
            LanguageModel("vi", "베트남어"),
        )
    }
}