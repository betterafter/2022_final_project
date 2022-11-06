package com.kuroutine.kulture.posting

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kuroutine.kulture.PICK_IMG_FROM_ALBUM
import com.kuroutine.kulture.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class PostingActivity : AppCompatActivity() {
    lateinit var imgbtn1: ImageButton
    lateinit var recyclerView: RecyclerView
    lateinit var submitButton: ImageButton
    lateinit var titleTextView: TextView
    lateinit var contentTextView: TextView

    private val postingViewModel by viewModels<PostingViewModel>()
    lateinit var adapter: MultiImgAdapter

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        imgbtn1 = findViewById<ImageButton>(R.id.imgbtn_activity_posting)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerview_pst)
        submitButton = findViewById(R.id.btn_posting_finish)
        titleTextView = findViewById(R.id.et_posting_title)
        contentTextView = findViewById(R.id.et_posting_details)

        postingViewModel.imageList.value?.let {
            adapter = MultiImgAdapter(it, this)
        } ?: run {
            adapter = MultiImgAdapter(listOf(), this)
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        initListener()
    }

    @SuppressLint("IntentReset")
    fun initListener() {
        imgbtn1.setOnClickListener {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )

            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            photoPickerIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            //여러장의 이미지를 앨범에서 가져올수 있게해줌.

            startActivityForResult(photoPickerIntent, PICK_IMG_FROM_ALBUM)
        }

        submitButton.setOnClickListener {
            postingViewModel.postQuestion(
                titleTextView.text.toString(),
                contentTextView.text.toString()
            ) {
                this.finish()
            }
        }
    }

    //startActivityForResult가 deprecated되는 것은 추후 수정이 필요함.
    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMG_FROM_ALBUM)
            if (resultCode == Activity.RESULT_OK) {
                if (postingViewModel.imageListSize() >= 5) {
                    Toast.makeText(
                        applicationContext,
                        "사진은 최대 5장까지 선택 가능합니다.", Toast.LENGTH_LONG
                    ).show()

                    return
                }

                if (data?.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        postingViewModel.addImageList(imageUri)
                    }
                } else {
                    data?.data?.let { uri ->
                        val imageUri: Uri? = data.data
                        if (imageUri != null) {
                            postingViewModel.addImageList(imageUri)
                        }
                    }
                }
                postingViewModel.imageList.value?.let { adapter.submitList(it) }
                adapter.notifyDataSetChanged()
            }
    }
}




