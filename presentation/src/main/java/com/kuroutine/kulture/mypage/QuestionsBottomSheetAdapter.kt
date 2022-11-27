package com.kuroutine.kulture.mypage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.dto.DashboardQuestionModel
import com.example.domain.dto.LanguageModel
import com.example.domain.dto.QuestionModel
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ItemLanguageBinding
import com.example.kuroutine.databinding.ItemQuestionBinding
import com.google.android.material.internal.ContextUtils.getActivity

class QuestionsBottomSheetAdapter(
    private val selectCallback: (DashboardQuestionModel) -> Unit,
    private val viewModel: MyPageViewModel
) : ListAdapter<DashboardQuestionModel, QuestionsBottomSheetAdapter.ViewHolder>(DiffUtils()) {

    class ViewHolder(
        private val binding: ItemQuestionBinding,
        private val callback: (DashboardQuestionModel) -> Unit,
        private val viewModel: MyPageViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("RestrictedApi")
        fun bind(data: DashboardQuestionModel) {

            Glide.with(binding.root.context)
                .load(if (data.imageList?.first() != null) data.imageList?.first() else R.drawable.ic_noimage)
                .circleCrop()
                .into(binding.ivHomeThumbnail)
            binding.tvHomeQuestion.text = data.title
            binding.tvItemQuestionTime.text = data.timestamp
            binding.tvHomeStateLabel.text = if (data.questionState == "질문 완료") {
                "질문 완료"
            } else {
                "질문 중"
            }

            //질문상태 변경 alertdialog 띄움
            binding.imageView5.setOnClickListener {
                // dialog 띄우기
                val builder = AlertDialog.Builder(it.context, R.style.MyAlertDialogStyle)
                    .setTitle("질문상태를 선택하세요")
                    .setNegativeButton("질문 중") { dialog, which ->
                        binding.tvHomeStateLabel.text = "질문 중" //질문상태 변경
                        viewModel.updateQuestionState(data.id, "질문 중")
                    }
                    .setPositiveButton("질문 완료") { dialog, which ->
                        binding.tvHomeStateLabel.text = "질문 완료" //질문상태 변경
                        viewModel.updateQuestionState(data.id, "질문 완료")
                    }
                    .create()
                    .show()

            }

        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class DiffUtils : DiffUtil.ItemCallback<DashboardQuestionModel>() {
        override fun areItemsTheSame(oldItem: DashboardQuestionModel, newItem: DashboardQuestionModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DashboardQuestionModel, newItem: DashboardQuestionModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = currentList[position]
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            selectCallback,
            viewModel
        )
    }
}
