package com.kuroutine.kulture.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.LanguageModel
import com.example.kuroutine.R
import com.example.kuroutine.databinding.LayoutBottomsheetLanguageBinding
import com.example.kuroutine.databinding.LayoutBottomsheetQuestionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class QuestionsBottomSheet(private var adapter: QuestionsBottomSheetAdapter) : BottomSheetDialogFragment() {
    private lateinit var bottomSheetViewModel: QuestionsBottomSheetViewModel
    private lateinit var binding: LayoutBottomsheetQuestionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomSheetViewModel =
            ViewModelProvider(this).get(QuestionsBottomSheetViewModel::class.java)

        binding = LayoutBottomsheetQuestionsBinding.inflate(inflater, container, false).apply {
            questionViewModel = bottomSheetViewModel
            lifecycleOwner = this@QuestionsBottomSheet
        }
        val root: View = binding.root
        bottomSheetViewModel.setQuestionList()
        bottomSheetViewModel.questionList.observe(this) {
            adapter.submitList(it)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.rv_bottomsheet_questions)
        rv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.adapter = adapter
    }
}