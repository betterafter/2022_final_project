package com.kuroutine.kulture.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.LanguageModel
import com.example.kuroutine.databinding.ItemLanguageBinding

class BottomSheetAdapter(
    private val selectCallback: (LanguageModel) -> Unit
) : ListAdapter<LanguageModel, BottomSheetAdapter.ViewHolder>(DiffUtils()) {

    class ViewHolder(
        private val binding: ItemLanguageBinding,
        private val callback: (LanguageModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LanguageModel) {
            binding.tvItemLanguage.text = data.text
            binding.tvItemLanguage.setOnClickListener {
                callback(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class DiffUtils : DiffUtil.ItemCallback<LanguageModel>() {
        override fun areItemsTheSame(oldItem: LanguageModel, newItem: LanguageModel): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: LanguageModel, newItem: LanguageModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = currentList[position]
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            selectCallback
        )
    }
}
