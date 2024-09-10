package com.example.programingdemo.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LoginActivityMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        signUpButton.setOnClickListener {
            registerEmailAndPassword()
        }
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        emailEditText = binding.edtEmail
        passwordEditText = binding.edtPassword
        signUpButton = binding.btnSave
    }

    private fun registerEmailAndPassword() {
        val email = emailEditText.text.trim().toString()
        val password = passwordEditText.text.trim().toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            signUpWithEmailPassword(email, password)
        } else {
            Toast.makeText(
                this@LoginActivity,
                getString(R.string.please_enter_email_and_password),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun signUpWithEmailPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(
                        getString(R.string.loginactivity),
                        getString(R.string.createuserwithemail_success)
                    )
                    Toast.makeText(
                        this,
                        getString(R.string.registration_success), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.w(
                        getString(R.string.loginactivity),
                        getString(R.string.createuserwithemail_failure), task.exception
                    )
                    Toast.makeText(
                        this,
                        getString(R.string.registration_failed, task.exception?.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
