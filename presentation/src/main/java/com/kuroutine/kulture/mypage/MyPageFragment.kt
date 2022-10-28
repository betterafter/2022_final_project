package com.kuroutine.kulture.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kuroutine.R
import com.example.kuroutine.databinding.FragmentMypageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private lateinit var myPageViewModel: MyPageViewModel
    private var _binding: FragmentMypageBinding? = null

    private lateinit var bottomSheetDialog: BottomSheetDialog

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
    }

    private fun initListener() {
        binding.llMypageLanguageSelection.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun createBottomSheetDialog() {
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_bottomsheet, null)
        bottomSheetDialog = BottomSheetDialog(this@MyPageFragment.requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)
    }

    private fun showBottomSheet() {
        bottomSheetDialog.show()
    }
}