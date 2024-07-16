package com.example.deadrobotrestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var btn_back: Button
    private lateinit var btn_addToCart: Button
    private lateinit var productTitle: TextView
    private lateinit var quantityEditText: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_back = findViewById(R.id.btnBack)
        productTitle = findViewById(R.id.prodTitle)
        quantityEditText = findViewById(R.id.edit_Quantity)
        btn_addToCart = findViewById(R.id.addCart)

        // Get data from Intent
        val id = intent.getIntExtra("product_id", -1) // Default to -1 if not found
        val title = intent.getStringExtra("title") ?: "No title"
        val longDescription = intent.getStringExtra("longDescription") ?: "No description"
        val image = intent.getStringExtra("image") ?: ""
        val calories = intent.getIntExtra("calories", -69) // Default to -1f if not found
        val rating = intent.getFloatExtra("rating", -69f) // Default to -1 if not found
        val prodPrice = intent.getIntExtra("price",-69)


        Log.d("productDeet", "ProductDetail:Product ID: ${id}, ${title}, Calories: ${calories}, Ratings: ${rating}, Price: ${prodPrice}", );
        // Set data to views
        binding.prodTitle.text = title
        binding.prodDeets.text = longDescription
        binding.txtRate.text = rating.toString() + "⭐"
        binding.txtRate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        binding.txtCals.text = calories.toString() + " Calories"
        binding.txtCals.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

        // Load image
        if (image != null) {
            if (image.contains("gs://")) {
                val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image)
                Glide.with(this)
                    .load(storageReference)
                    .into(binding.imgBurger)
            } else {
                Glide.with(this)
                    .load(image)
                    .into(binding.imgBurger)
            }
        }
        //navigate back
        btn_back.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        btn_addToCart.setOnClickListener {
            val title = productTitle.text.toString()
            val quantity = quantityEditText.text.toString().toIntOrNull() ?: 0


            if (quantity == null) {
                Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (quantity > 5) {
                Toast.makeText(this, "Quantity should be 5 or lower", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (quantity <= 0) {
                Toast.makeText(this, "Quantity should be greater than 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val price = prodPrice

            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("PRODUCT_TITLE", title)
            intent.putExtra("PRODUCT_QUANTITY", quantity)
            intent.putExtra("PRODUCT_PRICE", price)

            Log.d("cartDeet", "title: ${title}, Quantity: ${quantity}, Price: ${price}" );

            startActivity(intent)
        }


        }
    }

