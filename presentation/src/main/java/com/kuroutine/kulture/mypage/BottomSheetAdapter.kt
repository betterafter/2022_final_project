import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.LanguageModel
import com.example.kuroutine.R

//package com.kuroutine.kulture.mypage
//
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.example.domain.dto.LanguageModel
//import com.example.kuroutine.databinding.ItemLanguageBinding
//
//class BottomSheetAdapter : ListAdapter<LanguageModel, BottomSheetAdapter.ViewHolder>(DiffUtils()) {
//
//    class ViewHolder(
//        private val binding: ItemLanguageBinding,
//    ) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(data: LanguageModel) {
//            Log.d("[keykat]", "$data")
//            binding.tvItemLanguage.text = data.text
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return currentList.size
//    }
//
//    class DiffUtils : DiffUtil.ItemCallback<LanguageModel>() {
//        override fun areItemsTheSame(oldItem: LanguageModel, newItem: LanguageModel): Boolean {
//            return oldItem.code == newItem.code
//        }
//
//        override fun areContentsTheSame(oldItem: LanguageModel, newItem: LanguageModel): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val data = currentList[position]
//        Log.d("[keykat]", "SDFDSFSADFSDFDSFSDA")
//        holder.bind(data)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        Log.d("[keykat]", "oncreateviewholder")
//        return ViewHolder(
//            ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
//        )
//    }
//}

class BottomSheetAdapter : RecyclerView.Adapter<BottomSheetAdapter.Holder>() {
    private var itemList: MutableList<LanguageModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(items: MutableList<LanguageModel>) {
        if (items.isNotEmpty()) {
            itemList = items
            notifyDataSetChanged()
        }
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: LanguageModel) {
            Log.d("[keykat]","DSFSDFDSAFSDFDSAFDSAFSDAFSADFDSAFAS")
            view.findViewById<TextView>(R.id.tv_item_language).text = item.text
        }
    }
}