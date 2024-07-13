package com.example.deadrobotrestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        // my vals from views
        val fullName = findViewById<EditText>(R.id.edit_fullName)
        val email = findViewById<EditText>(R.id.edit_Email)
        val password = findViewById<EditText>(R.id.edit_Password)
        val confirmPassword = findViewById<EditText>(R.id.edit_ConfirmPassword)
        val registerBtn = findViewById<Button>(R.id.register_btn)

        val textSignIn = findViewById<TextView>(R.id.toLogin)

        textSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerBtn.setOnClickListener {
            //text validation
            val fullName = fullName?.text.toString().trim()
            val email = email?.text.toString().trim()
            val password = password?.text.toString().trim()
            val confirmPassword = confirmPassword?.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // validate name
            if (fullName.length < 3) {
                Toast.makeText(this, "Full name should be at least 3 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // validate email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // validate password
            if (password.length < 6) {
                Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!password.matches(".*[A-Z].*".toRegex())) {
                Toast.makeText(this, "Password should contain at least one uppercase letter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!password.matches(".*[a-z].*".toRegex())) {
                Toast.makeText(this, "Password should contain at least one lowercase letter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!password.matches(".*[@#\$%^&+=].*".toRegex())) {
                Toast.makeText(this, "Password should contain at least one special character (@#\$%^&+=)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            // create user
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // sign up Success
                        val user = auth?.currentUser
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // failed Sign-up
                        Toast.makeText(baseContext, "Registration failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

}