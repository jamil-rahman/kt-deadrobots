package com.example.deadrobotrestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deadrobotrestaurant.databinding.ActivityMainBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var adapter : ProductAdapter? = null
    private lateinit var btn_cart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_cart = findViewById(R.id.btn_view_cart)

        val query = FirebaseDatabase.getInstance().reference.child("products")
        val option = FirebaseRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()
        adapter = ProductAdapter(option)

        val rView : RecyclerView = findViewById(R.id.productRecyclerView)

        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter

        btn_cart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            // Start CartActivity
            startActivity(intent)
        }
    }
    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }
}
