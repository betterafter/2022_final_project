package com.kuroutine.kulture

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.kuroutine.databinding.DialogCommonBinding
import com.example.kuroutine.databinding.DialogDetailBinding
import com.example.kuroutine.databinding.FragmentHomeBinding
import com.kuroutine.kulture.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*

class CommonDialog(val text: String) : DialogFragment() {

    private var _binding: DialogCommonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogCommonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()

        return root
    }

    private fun init() {
        binding.btnConfirm.setOnClickListener {
            this.dismiss()
        }

        binding.txtContents.text = text
    }
}

class DetailDialog(
    val title: String,
    val text: String,
    val confirmCallback: () -> Unit,
    val dismissCallback: () -> Unit
    ) : DialogFragment() {

    private var _binding: DialogDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()

        return root
    }

    private fun init() {
        binding.btnConfirm.setOnClickListener {
            confirmCallback()
            this.dismiss()
        }

        binding.btnDismiss.setOnClickListener {
            dismissCallback()
            this.dismiss()
        }

        binding.txtTitle.text = title
        binding.txtContents.text = text
    }
}