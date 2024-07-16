package com.example.deadrobotrestaurant

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.deadrobotrestaurant.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    private lateinit var imageLogo: ImageView
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var textSignUp: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Get references to views
        imageLogo = findViewById(R.id.image_logo)
        editEmail = findViewById(R.id.edit_Email)
        editPassword = findViewById(R.id.edit_Password)
        btnSignIn = findViewById(R.id.btnSignIn)
        textSignUp = findViewById(R.id.text_SignUp)

        btnSignIn.setOnClickListener { signIn() }
        textSignUp.setOnClickListener { navigateToSignUp() }

        }

    private fun signIn() {
        val email = editEmail.text.toString().trim()
        val password = editPassword.text.toString().trim()

        // text validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // sign in with email and password
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // sign in success,
                    Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()
                    // navigate to main activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // signIn Fails
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

        private fun navigateToSignUp() {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

