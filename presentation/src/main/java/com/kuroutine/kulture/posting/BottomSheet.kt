package com.kuroutine.kulture.posting

import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.R
import com.example.kuroutine.databinding.LayoutBottomsheetLocationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet(private var adapter: BottomSheetAdapter) : BottomSheetDialogFragment() {
    private val postingViewModel by viewModels<PostingViewModel>(
        ownerProducer = { requireActivity() }
    )
    private lateinit var binding: LayoutBottomsheetLocationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = LayoutBottomsheetLocationBinding.inflate(inflater, container, false).apply {
            viewModel = postingViewModel
            lifecycleOwner = this@BottomSheet
        }
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.rv_bottomsheet_language)
        rv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.adapter = adapter
    }

    fun getLocations(list: List<Address>?) {
        adapter.submitList(list)
    }
}