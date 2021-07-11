package com.example.imageload

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.imageload.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var REQUEST_CODE = 101
    lateinit var bitmap: Bitmap
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        button.isEnabled = false


        binding.button.setOnClickListener {

            getRuntimePermission()
//            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(i,REQUEST_CODE)
        }
    }

    private fun getRuntimePermission() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                //permission is denied
                val permission = arrayOf(android.Manifest.permission.CAMERA);
                //show popup to request run time permission
                requestPermissions(permission,REQUEST_CODE);
            }else{
                //permission already granted
                pickImageFromGallery();
            }
        }else{
            //Device OS is < Marshmallow
            pickImageFromGallery();
        }
    }

    private fun pickImageFromGallery() {
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i,REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        (requestCode == REQUEST_CODE)
        requestCode.let{
            var photo:Bitmap? = null
            photo?.let {
                photo = data?.extras?.get("data") as Bitmap
                binding.imageView.setImageBitmap(photo)
                photo = photo?.copy(Bitmap.Config.ARGB_8888,true)
                bitmap = photo!!
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE -> {
                if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}