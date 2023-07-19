package com.example.getsetdata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.firestore.FirebaseFirestore

class FetchDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_data)

        val list=findViewById<ListView>(R.id.list_item)

        val db= FirebaseFirestore.getInstance()
        db.collection("users").get().addOnSuccessListener {
            val dataModels = it.toObjects(DataModel::class.java)
            list.adapter=ListViewAdapter(this,dataModels)

        }.addOnFailureListener {


        }

    }
}