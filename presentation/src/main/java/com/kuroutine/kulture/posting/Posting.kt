package com.kuroutine.kulture.posting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.android.synthetic.main.activity_posting.*
import kotlinx.android.synthetic.main.activity_posting.view.*
import java.text.SimpleDateFormat
import java.util.Date


class Posting : AppCompatActivity() {
    var list = ArrayList<Uri>()
    val adapter = MultiImgAdapter(list, this)

    val auth = FirebaseAuth.getInstance()
    var PICK_IMG_FROM_ALBUM = 0
    var db = FirebaseFirestore.getInstance()
    var storage: FirebaseStorage? = null
    var firestore: FirebaseFirestore? = null
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        var imgbtn1 = findViewById<ImageButton>(R.id.imgbtn_activity_posting)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerview_pst)


        ActivityCompat.requestPermissions(this,arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE),1)



        imgbtn1.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            photoPickerIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            //여러장의 이미지를 앨범에서 가져올수 있게해줌.

            startActivityForResult(photoPickerIntent, PICK_IMG_FROM_ALBUM)
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

 //startActivityForResult가 deprecated되는 것은 추후 수정이 필요함.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMG_FROM_ALBUM)
            if(resultCode == Activity.RESULT_OK) {
                list.clear()

                if(data?.clipData != null) {
                    val count = data.clipData!!.itemCount
                    if(count > 5) {
                        Toast.makeText(applicationContext,
                            "사진은 최대 5장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                        return
                    }
                    for(i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        list.add(imageUri)
                    }
                } else {
                    data?.data?.let { uri ->
                    val imageUri : Uri? = data?.data
                        if(imageUri != null) {
                            list.add(imageUri)
                        }
                }
            }
                adapter.notifyDataSetChanged()
            }
 }

    private fun contentUpload(view: RecyclerView) {

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMAGE_" + timestamp + "_.png"
        val storageRef=storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(imageUri!!)?.continueWithTask() {
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener {
        Toast.makeText(this, "성공적으로 업로드 하였습니다.", Toast.LENGTH_LONG).show()
        }
    }

    private fun upload(uri: Uri, imageFileName: String){

        val editText1 = findViewById<EditText>(R.id.et_posting_title)
        val editText2 = findViewById<EditText>(R.id.et_posting_details)

        val contentDto = ContentDto(
                imageUrl = uri.toString(),
                title = editText1.text.toString(),
                details = editText2.text.toString(),
                timestamp = System.currentTimeMillis().toLong(),
                uid = auth.uid.orEmpty(),
                imageStorage = imageFileName
        )

        firestore?.collection("images")?.document()?.set(contentDto)

        setResult(Activity.RESULT_OK)
        finish()
    }
        }




