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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet(private var adapter: BottomSheetAdapter) : BottomSheetDialogFragment() {
    private lateinit var bottomSheetViewModel: BottomSheetViewModel
    private lateinit var binding: LayoutBottomsheetLanguageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomSheetViewModel =
            ViewModelProvider(this).get(BottomSheetViewModel::class.java)

        binding = LayoutBottomsheetLanguageBinding.inflate(inflater, container, false).apply {
            viewModel = bottomSheetViewModel
            lifecycleOwner = this@BottomSheet
        }
        val root: View = binding.root
        bottomSheetViewModel.setLanguageList()
        bottomSheetViewModel.languageList.observe(this) {
            adapter.submitList(it)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.rv_bottomsheet_language)
        rv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.adapter = adapter
    }
}