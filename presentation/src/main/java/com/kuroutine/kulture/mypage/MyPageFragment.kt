package com.kuroutine.kulture.mypage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.domain.entity.DashboardQuestionModel
import com.example.domain.entity.LanguageModel
import com.example.kuroutine.R
import com.example.kuroutine.databinding.FragmentMypageBinding
import com.kuroutine.kulture.PICK_IMG_FROM_ALBUM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private lateinit var myPageViewModel: MyPageViewModel
    private var _binding: FragmentMypageBinding? = null

    private lateinit var bottomSheetDialog: BottomSheet
    private lateinit var questionsBottomSheetDialog: QuestionsBottomSheet
    private val binding get() = _binding!!

    private lateinit var adapter2: QuestionsBottomSheetAdapter

    private lateinit var contxt: Context

    private var alreadyChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contxt = this.requireContext()
    }

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

    @SuppressLint("IntentReset")
    private fun initListener() {
        myPageViewModel.currentUser.observe(viewLifecycleOwner) {
            val profile = if (it?.profile != null && it.profile != "") it.profile else (R.drawable.icon_profile)
            if (profile == R.drawable.icon_profile) {
                binding.ivMypageUserpic.setColorFilter(ContextCompat.getColor(contxt, R.color.main_color))
            } else {
                binding.ivMypageUserpic.setColorFilter(Color.TRANSPARENT)
            }

            binding.tvMypageNumberofpoints.text = it?.userXp.toString()

            Glide.with(contxt)
                .load(profile)
                .circleCrop()
                .into(binding.ivMypageUserpic)

            adapter2.submitList(it?.questionList)
        }

        binding.llMypageLanguageSelection.setOnClickListener {
            showBottomSheet()
        }

        binding.llMypageMyquestions.setOnClickListener {
            showQuestionBottomSheet()
        }

        binding.ivMypageUserpic.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(contxt, Manifest.permission.READ_EXTERNAL_STORAGE)
            if(permission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 1
                )
            } else {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                photoPickerIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                //여러장의 이미지를 앨범에서 가져올수 있게해줌.

                startActivityForResult(photoPickerIntent, PICK_IMG_FROM_ALBUM)
            }
        }
    }

    private fun createBottomSheetDialog() {
        val adapter = BottomSheetAdapter(selectCallback = ::selectCallback)
        bottomSheetDialog = BottomSheet(adapter)

        adapter2 = QuestionsBottomSheetAdapter(selectCallback = ::selectCallback2, myPageViewModel)
        questionsBottomSheetDialog = QuestionsBottomSheet(adapter2)
    }

    private fun showBottomSheet() {
        bottomSheetDialog.show(parentFragmentManager, "TAG")
    }

    private fun showQuestionBottomSheet() {
        questionsBottomSheetDialog.show(parentFragmentManager, "TAG")
    }

    private fun selectCallback(model: LanguageModel) {
        myPageViewModel.updateUserLanguage(model)
        bottomSheetDialog.dismiss()
    }

    private fun selectCallback2(model: DashboardQuestionModel) {
        questionsBottomSheetDialog.dismiss()
    }

    //startActivityForResult가 deprecated되는 것은 추후 수정이 필요함.
    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMG_FROM_ALBUM)
            if (resultCode == Activity.RESULT_OK) {
                if (data?.clipData != null) {
                    if (data.clipData!!.itemCount >= 1) {
                        val imageUri = data.clipData!!.getItemAt(0).uri
                        Glide.with(contxt)
                            .load(imageUri)
                            .circleCrop()
                            .into(binding.ivMypageUserpic)

                        myPageViewModel.updateUserProfile(imageUri)
                    } else {
                        Toast.makeText(context, "사진을 골라주세요", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(context, "사진을 골라주세요", Toast.LENGTH_SHORT).show()
                }
            }
    }
}