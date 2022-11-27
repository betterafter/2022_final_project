package com.kuroutine.kulture.posting

import android.annotation.SuppressLint
import android.location.Address
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.databinding.ItemLocationBinding

class BottomSheetAdapter(
    private val selectCallback: (String) -> Unit
) : ListAdapter<Address, BottomSheetAdapter.ViewHolder>(DiffUtils()) {

    class ViewHolder(
        private val binding: ItemLocationBinding,
        private val callback: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Address) {
            binding.tvItemLocation.text = data.getAddressLine(0)
            binding.tvItemLocation.setOnClickListener {
                callback(data.getAddressLine(0))
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class DiffUtils : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.featureName == newItem.featureName
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = currentList[position]
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            selectCallback
        )
    }
}
