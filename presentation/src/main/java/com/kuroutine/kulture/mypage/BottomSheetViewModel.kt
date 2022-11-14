package com.kuroutine.kulture.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.LanguageModel
import com.example.domain.usecase.user.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor() : ViewModel() {
    private val _languageList = MutableLiveData<List<LanguageModel>?>().apply { value = null }
    val languageList: LiveData<List<LanguageModel>?> = _languageList

    fun setLanguageList() {
        _languageList.value = listOf(
            LanguageModel("en", "영어"),
            LanguageModel("fr", "프랑스어"),
            LanguageModel("af", "아프리카어"),
            LanguageModel("sq", "알바니아어"),
            LanguageModel("be", "벨라루시아어"),
            LanguageModel("ar", "아라비아어"),
            LanguageModel("bn", "벵갈어"),
            LanguageModel("bg", "불가리아어"),
            LanguageModel("ca", "카탈루냐어"),
            LanguageModel("zh", "중국어"),
            LanguageModel("hr", "크로아티아어"),
            LanguageModel("cs", "체코어"),
            LanguageModel("da", "덴마크어"),
            LanguageModel("nl", "네덜란드어"),
            LanguageModel("eo", "에스페란토 (인공어)"),
            LanguageModel("et", "에스토니아어"),
            LanguageModel("fi", "핀란드어"),
            LanguageModel("gl", "갈라시아어"),
            LanguageModel("ka", "조지아어"),
            LanguageModel("de", "독일어"),
            LanguageModel("el", "그리스어"),
            LanguageModel("gu", "구자라트어"),
            LanguageModel("ht", "아이티어"),
            LanguageModel("he", "히브리어"),
            LanguageModel("hi", "힌디어"),
            LanguageModel("hu", "헝가리어"),
            LanguageModel("is", "아이슬란드어"),
            LanguageModel("id", "인도네시아어"),
            LanguageModel("ga", "아일랜드어"),
            LanguageModel("it", "이탈리아어"),
            LanguageModel("ja", "일본어"),
            LanguageModel("kn", "칸나다어"),
            LanguageModel("ko", "한국어"),
            LanguageModel("lv", "라트비아어"),
            LanguageModel("lt", "리투아니아어"),
            LanguageModel("mk", "마케도니아어"),
            LanguageModel("ms", "말레이시아어"),
            LanguageModel("mt", "몰타어"),
            LanguageModel("mr", "마라티어"),
            LanguageModel("no", "노르웨이어"),
            LanguageModel("fa", "페르시아어"),
            LanguageModel("pl", "폴란드어"),
            LanguageModel("pt", "포르투갈어"),
            LanguageModel("ro", "로마어"),
            LanguageModel("ru", "러시아어"),
            LanguageModel("sk", "슬로바키아어"),
            LanguageModel("es", "스페인어"),
            LanguageModel("sw", "스와힐리어"),
            LanguageModel("sv", "스웨덴어"),
            LanguageModel("tl", "타갈로그어"),
            LanguageModel("ta", "타밀어"),
            LanguageModel("te", "텔루구어"),
            LanguageModel("th", "대만어"),
            LanguageModel("tr", "터키어"),
            LanguageModel("uk", "우크라이나어"),
            LanguageModel("ur", "우르두어"),
            LanguageModel("vi", "베트남어"),
            LanguageModel("cy", "웨일스어"),
        )
    }
}