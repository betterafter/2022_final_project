package com.kuroutine.kulture.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.dto.LanguageModel
import com.example.domain.dto.QuestionModel
import com.example.domain.usecase.user.UserUsecase
import com.example.kuroutine.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsBottomSheetViewModel @Inject constructor(): ViewModel() {
    private val _questionList = MutableLiveData<List<QuestionModel>?>().apply { value = null }
    val questionList: LiveData<List<QuestionModel>?> = _questionList

    fun setQuestionList() {
//        _questionList.value = listOf(
//            // 질문사진,     질문제목,     질문등록시간,      질문상태(FALSE==질문중, TRUE==질문완료)
//            QuestionModel(R.drawable.korea2, "title1", "2022/01/01", false),
//            QuestionModel(R.drawable.ic_noimage, "title2", "2022/02/01", false),
//            QuestionModel(R.drawable.fan1, "title3", "2022/03/01", true),
//            QuestionModel(R.drawable.trad4, "title4", "2022/04/01", true),
//            QuestionModel(R.drawable.ic_noimage, "title5", "2022/05/01", false),
//            QuestionModel(R.drawable.trad6, "title6", "2022/06/01", true),
//        )
    }
}