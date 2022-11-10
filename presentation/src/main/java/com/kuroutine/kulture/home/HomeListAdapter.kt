package com.kuroutine.kulture.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.example.domain.dto.DashboardQuestionModel
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ItemHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeListAdapter(
    val moveToChatActivity: (String, String, Boolean) -> Unit,
    val viewModel: HomeViewModel
) : ListAdapter<DashboardQuestionModel, HomeListAdapter.ViewHolder>(DiffUtils()) {

    class ViewHolder(
        private val binding: ItemHomeBinding,
        private val viewModel: HomeViewModel,
        private val callback: (String, String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        suspend fun bind(data: DashboardQuestionModel) {
            if (!data.translatedState) {
                data.text = translate(data.title, data)
            }

            binding.tvHomeUserid.text = data.userName
            binding.tvHomeQuestion.text = data.text
            if (data.location == "") {
                binding.tvHomeLocation.text = "위치 비공개"
            } else {
                binding.tvHomeLocation.text = data.location + "에서"
            }

            data.imageList?.first()?.let {
                Glide.with(binding.root.context).load(data.imageList?.first())
                    //.transform(GranularRoundedCorners(30F, 0F, 0F, 30F))
                    .circleCrop()
                    .into(binding.ivHomeThumbnail)
            } ?: run {
                Glide.with(binding.root.context).load(R.drawable.ic_noimage)
                    //.transform(GranularRoundedCorners(30F, 0F, 0F, 30F))
                    .circleCrop()
                    .into(binding.ivHomeThumbnail)
            }

            viewModel.getUserProfile(data.uid) {
                if (it == "") {
                    Glide.with(binding.root.context).load(R.drawable.ic_baseline_account_circle_24)
                        .circleCrop()
                        .into(binding.ivHomeUserThumbnail)
                } else {
                    Glide.with(binding.root.context).load(it)
                        .circleCrop()
                        .into(binding.ivHomeUserThumbnail)
                }
            }

            binding.tvHomeLikeNum.text = data.likeCount

            binding.cvHomeItem.setOnClickListener {
                callback(data.id, data.uid, data.isPrivate)
            }
        }

        suspend fun translate(target: String, data: DashboardQuestionModel): String {
            data.translatedState = true
            val langCode = viewModel.checkLanguage(target)
            return if (langCode == viewModel.language.value) {
                ""
            } else {
                viewModel.getTranslatedText(target, langCode) ?: target
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel,
            moveToChatActivity
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class DiffUtils : DiffUtil.ItemCallback<DashboardQuestionModel>() {
        override fun areItemsTheSame(oldItem: DashboardQuestionModel, newItem: DashboardQuestionModel): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: DashboardQuestionModel, newItem: DashboardQuestionModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = currentList[position]
        CoroutineScope(Dispatchers.Main).launch { holder.bind(data) }
    }
}