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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var btn_back: Button
    private lateinit var btn_addToCart: Button
    private lateinit var productTitle: TextView
    private lateinit var quantityEditText: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
//        val user = FirebaseAuth.getInstance().currentUser
//        Log.d("auth", "${user}")
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_back = findViewById(R.id.btnBack)
        productTitle = findViewById(R.id.prodTitle)
        quantityEditText = findViewById(R.id.edit_Quantity)
        btn_addToCart = findViewById(R.id.addCart)

        // Get data from the Intent
        val id = intent.getIntExtra("product_id", -1)
        val title = intent.getStringExtra("title") ?: "No title"
        val longDescription = intent.getStringExtra("longDescription") ?: "No description"
        val image = intent.getStringExtra("image") ?: ""
        val calories = intent.getIntExtra("calories", -69)
        val rating = intent.getFloatExtra("rating", -69f)
        val prodPrice = intent.getIntExtra("price",-69)


        Log.d("productDeet", "ProductDetail:Product ID: ${id}, ${title}, Calories: ${calories}, Ratings: ${rating}, Price: ${prodPrice}", );
        // Set the data to views
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
        //navigate back on back button
        btn_back.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        btn_addToCart.setOnClickListener {
            val quantityStr = quantityEditText.text.toString()
            val quantity = if (quantityStr.isNotEmpty()) quantityStr.toInt() else 0

            if (quantity <= 0 ) {
                Toast.makeText(this, "You have to order at least 1", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (quantity > 5 ) {
                Toast.makeText(this, "You can order 5 at max", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val productPrice = prodPrice * quantity
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = user.uid
            val database = FirebaseDatabase.getInstance()
            val productRef = database.getReference("products").child(id.toString())
            val cartRef = database.getReference("cart").child(userId).child(id.toString())

            productRef.child("quantity").get().addOnSuccessListener { snapshot ->
                val currentQuantity = snapshot.getValue(Int::class.java) ?: 0
                if (currentQuantity < quantity) {
                    Toast.makeText(this, "Not enough stock available", Toast.LENGTH_SHORT).show()
                } else {
                    val newQuantity = currentQuantity - quantity
                    productRef.child("quantity").setValue(newQuantity)

                    val cartItem = mapOf(
                        "id" to id.toString(),
                        "title" to title,
                        "image" to image,
                        "quantity" to quantity,
                        "price" to productPrice
                    )
                    cartRef.setValue(cartItem).addOnSuccessListener {
                        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                    }
                    quantityEditText.setText("")
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to get product data", Toast.LENGTH_SHORT).show()
            }
        }

        }
    }

