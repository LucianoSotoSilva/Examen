package com.android.examen

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import com.android.examen.databinding.ActivitySearchcachorroBinding
import com.google.firebase.storage.StorageReference
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


class Searchcachorro : AppCompatActivity() {


    lateinit var  binding: ActivitySearchcachorroBinding
    lateinit var ImageUrl : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchcachorroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSeleccionar.setOnClickListener {

            selectImage()
        }
        binding.btnsubir.setOnClickListener {
            unploadImage()
        }
    }

    private fun unploadImage() {
     val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading File...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")

        storageReference.putFile(ImageUrl).addOnSuccessListener {

            binding.imageView.setImageURI(null)
            Toast.makeText( this@Searchcachorro,"Imagen Subida", Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing) progressDialog.dismiss()

        }.addOnFailureListener{
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText( this@Searchcachorro,"Algo Fallo", Toast.LENGTH_SHORT).show()

        }
    }

    private fun selectImage() {
       val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== 100 && resultCode == RESULT_OK){

            ImageUrl = data?.data!!
            binding.imageView.setImageURI(ImageUrl)




        }
    }


}