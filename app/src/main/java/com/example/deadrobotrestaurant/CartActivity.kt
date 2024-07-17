package com.example.deadrobotrestaurant
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    private lateinit var btnToOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        btnToOrder = findViewById(R.id.btnToOrder)


        btnToOrder.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            // Start OrderActivity
            startActivity(intent)
        }

    }


}
