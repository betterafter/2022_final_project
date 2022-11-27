package com.kuroutine.kulture.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.R
import com.example.kuroutine.databinding.LayoutBottomsheetQuestionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class QuestionsBottomSheet(private var adapter: QuestionsBottomSheetAdapter) : BottomSheetDialogFragment() {
    private val myPageViewModel by viewModels<MyPageViewModel>(
        ownerProducer = { requireActivity() }
    )
    private lateinit var binding: LayoutBottomsheetQuestionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LayoutBottomsheetQuestionsBinding.inflate(inflater, container, false).apply {
            questionViewModel = myPageViewModel
            lifecycleOwner = this@QuestionsBottomSheet
        }
        val root: View = binding.root

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