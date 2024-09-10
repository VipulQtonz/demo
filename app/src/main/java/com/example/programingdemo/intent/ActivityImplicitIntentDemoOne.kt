package com.example.programingdemo.intent

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityImplicitIntentDemoOneBinding

class ActivityImplicitIntentDemoOne : AppCompatActivity() {
    private lateinit var binding: ActivityImplicitIntentDemoOneBinding
    private lateinit var edtEmail: EditText
    private lateinit var edtSubject: EditText
    private lateinit var edtText: EditText
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImplicitIntentDemoOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ImplicitIntentDemoOneMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }

        init()
        addOnClickListener()

    }

    private fun init() {
        edtEmail = binding.edtEmail
        edtSubject = binding.edtSubject
        edtText = binding.edtText
        btnSubmit = binding.btnSubmit
    }

    private fun addOnClickListener() {
        btnSubmit.setOnClickListener {
            if (validation()) {
                if (checkForInternet(this)) {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${edtEmail.text}")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(edtEmail.text.toString()))
                        putExtra(Intent.EXTRA_SUBJECT, edtSubject.text.toString())
                        putExtra(Intent.EXTRA_TEXT, edtText.text.toString())
                    }
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        this,
                        R.string.internet_error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validation(): Boolean {
        val email = edtEmail.text.toString().trim()
        val subject = edtSubject.text.toString().trim()
        val body = edtText.text.toString().trim()

        if (email.isEmpty() || !isValidEmail(email)) {
            binding.edtEmail.error = getString(R.string.email_error)
            return false
        }

        if (subject.isEmpty()) {
            Toast.makeText(this, R.string.subject_error, Toast.LENGTH_SHORT).show()
            return false
        }

        if (body.isEmpty()) {
            Toast.makeText(this, R.string.body_error, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(Regex(emailPattern))
    }
}
