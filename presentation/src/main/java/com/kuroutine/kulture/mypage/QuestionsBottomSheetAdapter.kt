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
import com.example.domain.dto.LanguageModel
import com.example.domain.dto.QuestionModel
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ItemLanguageBinding
import com.example.kuroutine.databinding.ItemQuestionBinding
import com.google.android.material.internal.ContextUtils.getActivity

class QuestionsBottomSheetAdapter(
    private val selectCallback: (QuestionModel) -> Unit
) : ListAdapter<QuestionModel, QuestionsBottomSheetAdapter.ViewHolder>(DiffUtils()) {

    class ViewHolder(
        private val binding: ItemQuestionBinding,
        private val callback: (QuestionModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("RestrictedApi")
        fun bind(data: QuestionModel) {

            binding.ivHomeThumbnail.setImageResource(data.picture)
            binding.tvHomeQuestion.text = data.title
            binding.tvItemQuestionTime.text = data.time
            binding.tvHomeStateLabel.text = if(data.isOver){"질문완료"} else{"질문중"}

            //질문상태 변경 alertdialog 띄움
            binding.imageView5.setOnClickListener {
                // dialog 띄우기
                val builder = AlertDialog.Builder(it.context, R.style.MyAlertDialogStyle)
                    .setTitle("질문상태를 선택하세요")
                    .setNegativeButton("질문중", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            binding.tvHomeStateLabel.text = "질문중" //질문상태 변경
                        }
                    })
                    .setPositiveButton("질문완료", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            binding.tvHomeStateLabel.text = "질문완료" //질문상태 변경
                        }
                    })
                    .create()
                    .show()

            }

        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class DiffUtils : DiffUtil.ItemCallback<QuestionModel>() {
        override fun areItemsTheSame(oldItem: QuestionModel, newItem: QuestionModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: QuestionModel, newItem: QuestionModel): Boolean {
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
            selectCallback
        )
    }
}
