package com.example.deadrobotrestaurant
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deadrobotrestaurant.databinding.ActivityCartBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CartActivity : AppCompatActivity() {

    private lateinit var btnToOrder: Button
    private lateinit var binding: ActivityCartBinding
    private var adapter: CartAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_cart)


        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            finish()
            return
        }

        val userId = user.uid
        val query = FirebaseDatabase.getInstance().reference.child("cart").child(userId)
        val options = FirebaseRecyclerOptions.Builder<CartItem>()
            .setQuery(query, CartItem::class.java)
            .build()

        adapter = CartAdapter(options,userId)
        val rView : RecyclerView = findViewById(R.id.cartRecyclerView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter

        btnToOrder = findViewById(R.id.btnToOrder)


        btnToOrder.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            // Start OrderActivity
            startActivity(intent)
        }

    }
    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

}
