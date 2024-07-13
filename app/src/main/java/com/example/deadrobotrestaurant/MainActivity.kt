package com.example.deadrobotrestaurant

import android.os.Bundle
import android.util.Log
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
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = FirebaseDatabase.getInstance().reference.child("products")
        val option = FirebaseRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()
        adapter = ProductAdapter(option)
        val rView : RecyclerView = findViewById(R.id.cartRecyclerView)

        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter
    }
    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }
}
