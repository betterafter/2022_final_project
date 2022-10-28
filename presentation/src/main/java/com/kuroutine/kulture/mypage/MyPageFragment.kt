package com.kuroutine.kulture.mypage

import BottomSheetAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.LanguageModel
import com.example.kuroutine.R
import com.example.kuroutine.databinding.FragmentMypageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private lateinit var myPageViewModel: MyPageViewModel
    private var _binding: FragmentMypageBinding? = null

    private lateinit var bottomSheetDialog: BottomSheet
    private lateinit var bottomSheetList: RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myPageViewModel =
            ViewModelProvider(this).get(MyPageViewModel::class.java)

        _binding = FragmentMypageBinding.inflate(inflater, container, false).apply {
            viewModel = myPageViewModel
            lifecycleOwner = this@MyPageFragment
        }
        val root: View = binding.root

        init()
        initListener()

        return root
    }

    private fun init() {
        myPageViewModel.getUser()
        createBottomSheetDialog()
        myPageViewModel.setLanguageList()
    }

    private fun initListener() {
        binding.llMypageLanguageSelection.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun createBottomSheetDialog() {
        val adapter = BottomSheetAdapter()
        bottomSheetDialog = BottomSheet(adapter)

        myPageViewModel.languageList.value?.toMutableList()?.let { adapter.setItem(it) }

        adapter.setItem(listOf(
            LanguageModel("ko", "한국어"),
            LanguageModel("ja", "일본어"),
            LanguageModel("zh-CN","중국어 (간체)"),
            LanguageModel("zh-TW", "중국어 (번체)"),
            LanguageModel("hi", "힌디어"),
            LanguageModel("en", "영어"),
            LanguageModel("es", "스페인어"),
            LanguageModel("fr", "프랑스어"),
            LanguageModel("de", "독일어"),
            LanguageModel("pt", "포르투칼어"),
            LanguageModel("vi", "베트남어"),
            LanguageModel("id", "인도네시아어"),
            LanguageModel("fa", "페르시아어"),
            LanguageModel("ar", "아랍어"),
            LanguageModel("mm", "미얀마어"),
            LanguageModel("th", "태국어"),
            LanguageModel("ru", "러시아어"),
            LanguageModel("it", "이탈리아어"),
        ).toMutableList())

//        myPageViewModel.languageList.observe(viewLifecycleOwner) {
//            it?.toMutableList()?.let { it1 -> adapter.setItem(it1) }
//        }
    }

    private fun showBottomSheet() {
        bottomSheetDialog.show(parentFragmentManager, "TAG")
    }
}