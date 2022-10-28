package com.kuroutine.kulture.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kuroutine.databinding.FragmentMypageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private lateinit var myPageViewModel: MyPageViewModel
    private var _binding: FragmentMypageBinding? = null

    private lateinit var bottomSheetDialog: BottomSheet
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
    }

    private fun showBottomSheet() {
        bottomSheetDialog.show(parentFragmentManager, "TAG")
    }
}