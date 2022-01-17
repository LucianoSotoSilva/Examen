package com.android.examen.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.examen.Searchcachorro
import com.android.examen.databinding.ActivityPrincipalviewBinding



class Principalview : AppCompatActivity() {

    private val LOCATION__REQUEST_CODE = 0
    private lateinit var  binding:ActivityPrincipalviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPrincipalviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBuscar.setOnClickListener {
            val intento1 = Intent(this, MainActivity::class.java)
            startActivity(intento1)

            checkCameraPermission()
        }

        binding.btnCapturar.setOnClickListener {
            val intento1 = Intent(this, Searchcachorro::class.java)
            startActivity(intento1)
        }
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
            //El permiso no está aceptado.
        } else {
            //El permiso está aceptado.
        }
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION__REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION__REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                }
                return
            }
            else -> {
            }
        }
    }
}