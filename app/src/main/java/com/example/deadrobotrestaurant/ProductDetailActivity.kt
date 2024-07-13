package com.example.deadrobotrestaurant

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.example.deadrobotrestaurant.databinding.ActivityProductDetailBinding
import com.google.firebase.storage.FirebaseStorage

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from Intent
        val title = intent.getStringExtra("title")
        val longDescription = intent.getStringExtra("longDescription")
        val image = intent.getStringExtra("image")

        // Set data to views
        binding.prodTitle.text = title
        binding.prodDeets.text = longDescription

        // Load image
        if (image != null) {
            if (image.contains("gs://")) {
                val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image)
                Glide.with(this)
                    .load(storageReference)
                    .into(binding.imageView3)
            } else {
                Glide.with(this)
                    .load(image)
                    .into(binding.imageView3)
            }
        }

        }
    }

