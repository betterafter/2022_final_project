package com.kuroutine.kulture.posting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.LanguageModel
import com.example.kuroutine.R
import com.google.android.material.button.MaterialButtonToggleGroup
import com.kuroutine.kulture.CommonDialog
import com.kuroutine.kulture.mypage.BottomSheetAdapter
import com.kuroutine.kulture.PICK_IMG_FROM_ALBUM
import com.kuroutine.kulture.mypage.BottomSheet
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class PostingActivity : AppCompatActivity() {
    lateinit var imgbtn1: ImageButton
    lateinit var recyclerView: RecyclerView
    lateinit var submitButton: ImageButton
    lateinit var titleTextView: TextView
    lateinit var contentTextView: TextView
    lateinit var toggleButtonGroup: MaterialButtonToggleGroup
    lateinit var locationButton: ImageView
    lateinit var locationTextView: EditText

    private lateinit var bottomSheetDialog: com.kuroutine.kulture.posting.BottomSheet

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
        toggleButtonGroup = findViewById(R.id.tbg_posting_post_type)
        locationButton = findViewById(R.id.ib_posting_location)
        locationTextView = findViewById(R.id.tv_posting_location)

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
        createBottomSheetDialog()
    }

    @SuppressLint("IntentReset")
    fun initListener() {
        imgbtn1.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
            if(permission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 1
                )
            } else {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                photoPickerIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                //???????????? ???????????? ???????????? ???????????? ????????????.

                startActivityForResult(photoPickerIntent, PICK_IMG_FROM_ALBUM)
            }
        }

        submitButton.setOnClickListener {
            if (titleTextView.text.toString().length > 30) {
                CommonDialog("????????? 30??? ????????? ???????????????.").show(supportFragmentManager, "")
            } else {
                postingViewModel.postQuestion(
                    titleTextView.text.toString(),
                    contentTextView.text.toString(),
                    locationTextView.text.toString(),
                    toggleButtonGroup.checkedButtonId == R.id.btn_posting_private
                ) {
                    this.finish()
                }
            }
        }

        locationButton.setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionCheck == PackageManager.PERMISSION_DENIED) { //?????? ?????? ??????
                //?????? ?????? ??????
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            } else {
                val gpsTracker = GpsTracker(this@PostingActivity)

                val latitude: Double = gpsTracker.getLatitude()
                val longitude: Double = gpsTracker.getLongitude()

                val geocoder = Geocoder(this, Locale.getDefault())
                try {
                    val addresses: List<Address>? = geocoder.getFromLocation(
                        latitude,
                        longitude,
                        7
                    )
                    bottomSheetDialog.getLocations(addresses)
                    showBottomSheet()
                } catch (e: Exception) {
                    Log.d("[keykat]", "$e")
                }
            }
        }
    }

    private fun createBottomSheetDialog() {
        val adapter = BottomSheetAdapter(selectCallback = ::selectCallback)
        bottomSheetDialog = BottomSheet(adapter)
    }

    private fun showBottomSheet() {
        bottomSheetDialog.show(this.supportFragmentManager, "TAG")
    }

    private fun selectCallback(model: String) {
        locationTextView.setText(model)
        bottomSheetDialog.dismiss()
    }

    //startActivityForResult??? deprecated?????? ?????? ?????? ????????? ?????????.
    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMG_FROM_ALBUM)
            if (resultCode == Activity.RESULT_OK) {
                if (postingViewModel.imageListSize() >= 5) {
                    Toast.makeText(
                        applicationContext,
                        "????????? ?????? 5????????? ?????? ???????????????.", Toast.LENGTH_LONG
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




