package com.example.getsetdata

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class SetDataActivity : AppCompatActivity() {

      lateinit var imageView: ImageView
    lateinit var selectImgBtn: Button
     lateinit var uploadImgBtn: Button
     var storageRef=Firebase.storage.reference
     lateinit var uri:Uri


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_data)

        val db= FirebaseFirestore.getInstance()
        val name=findViewById<EditText>(R.id.name)
        val title=findViewById<EditText>(R.id.title)
        val born=findViewById<EditText>(R.id.born)
        val button=findViewById<Button>(R.id.button)
       val  updateBtn=findViewById<Button>(R.id.updateBtn)
        val  DeleteBtn=findViewById<Button>(R.id.DeleteBtn)
        val selectImgBtn = findViewById<Button>(R.id.select_img)
        val uploadImgBtn = findViewById<Button>(R.id.upload_img)
        val imageView = findViewById<ImageView>(R.id.imageView)


        val storageRef = Firebase.storage.reference;

        val galleryImage=registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageView.setImageURI(it)
                if (it != null) {
                    uri =it
                }
            }
        )

        selectImgBtn.setOnClickListener {
            galleryImage.launch("image/*")
        }


        uploadImgBtn.setOnClickListener {
            if (uri != null) {
                val fileName = System.currentTimeMillis().toString()
                val imageRef = storageRef.child("image/").child(fileName)

                imageRef.putFile(uri)
                    .addOnSuccessListener { taskSnapshot ->
                        // File uploaded successfully
                        Toast.makeText(this, "Upload success", Toast.LENGTH_SHORT).show()

                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                            val downloadUrl = downloadUri.toString()
                            // Process the download URL or save it to the database
                        }.addOnFailureListener { exception ->
                            Toast.makeText(this, exception.toString() + "download URL", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        // File upload failed, handle the error
                        Toast.makeText(this, "Upload image $exception", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }



        DeleteBtn.setOnClickListener {
            db.collection("users").document("2PuTRAkDKo1x0p18YGCb"
            ).delete().addOnSuccessListener {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
        }
        updateBtn.setOnClickListener {
            val user:Map<String,Any> = hashMapOf(
                "first" to name.text.toString(),
                "last" to title.text.toString(),
                "born" to born.text.toString()
            )
            db.collection("users").document("2PuTRAkDKo1x0p18YGCb"
                    ).update(user).addOnSuccessListener {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }

        }
        button.setOnClickListener {
            val user = hashMapOf(
                "first" to name.text.toString(),
                "last" to title.text.toString(),
                "born" to born.text.toString()
            )


            db.collection("users").add(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,FetchDataActivity::class.java))
                     name.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
        }





    }





}