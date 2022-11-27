package com.kuroutine.kulture.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.entity.DashboardQuestionModel
import com.example.kuroutine.R
import com.example.kuroutine.databinding.ItemHomeBinding
import com.kuroutine.kulture.CommonDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeListAdapter(
    val moveToChatActivity: (String, String, Boolean) -> Unit,
    val viewModel: HomeViewModel,
    private val fragmentManager: FragmentManager
) : ListAdapter<DashboardQuestionModel, HomeListAdapter.ViewHolder>(DiffUtils()) {

    class ViewHolder(
        private val binding: ItemHomeBinding,
        private val viewModel: HomeViewModel,
        private val callback: (String, String, Boolean) -> Unit,
        private val fragmentManager: FragmentManager
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        suspend fun bind(data: DashboardQuestionModel) {
            // 제목 초기화
            binding.tvHomeQuestion.text = data.translatedTitle

            binding.tvHomeUserid.text = data.userName

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
            binding.tvHomeStateLabel.text = data.questionState

            binding.cvHomeItem.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    if (data.uid != viewModel.currentUser.value?.uid) {
                        callback(data.id, data.uid, data.isPrivate)
                    } else {
                        val dialog = CommonDialog("자신의 게시물은 채팅내역에서 확인하세요.")
                        dialog.show(fragmentManager, "")
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel,
            moveToChatActivity,
            fragmentManager
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