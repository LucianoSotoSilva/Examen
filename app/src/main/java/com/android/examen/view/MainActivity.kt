package com.android.examen.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat

import androidx.recyclerview.widget.LinearLayoutManager
import com.android.examen.R
import com.android.examen.databinding.ActivityMainBinding
import com.android.examen.model.APIservice
import com.android.examen.model.DogAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.emptyList as emptyList1

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var adapter: DogAdapter

    private val dogImages = mutableListOf<String>()

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var  mDatabaseReference: DatabaseReference
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.svDogs.setOnQueryTextListener(this)
            initRecyclerView()

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            val mDatabaseReference = FirebaseDatabase.getInstance().getReference();

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->

                    mDatabaseReference.child("usuarios").push().child("longitud").setValue(location?.getLongitude());
                    mDatabaseReference.child("usuarios").push().child("latitud").setValue(location?.getLatitude());
                }
        }

    private fun initRecyclerView() {
        adapter = DogAdapter(dogImages)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun svDogs(query:String){
        CoroutineScope(Dispatchers.IO).launch {
        val call = getRetrofit().create(APIservice::class.java).getDogsByBreeds("$query/images")
        val puppis = call.body()
        runOnUiThread{
        if (call.isSuccessful){
            val images = puppis?.images ?: emptyList1()
            dogImages.clear()
            dogImages.addAll(images)
            adapter.notifyDataSetChanged()
        }else{
            showError()
        }
            hideKeyboard()
        }
    }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken,0)
    }

    private fun showError() {
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            svDogs(query.toLowerCase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }



}











