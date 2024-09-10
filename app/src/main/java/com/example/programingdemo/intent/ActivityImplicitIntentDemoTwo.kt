package com.example.programingdemo.intent

import android.content.ActivityNotFoundException
import android.content.Intent
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
import com.example.programingdemo.databinding.ActivityImplicitIntentDemoTwoBinding

class ActivityImplicitIntentDemoTwo : AppCompatActivity() {
    private lateinit var binding: ActivityImplicitIntentDemoTwoBinding
    private lateinit var edtUrl: EditText
    private lateinit var btnGoto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityImplicitIntentDemoTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.llExplicitIntentDemoTwoMain)) { v, insets ->
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
        edtUrl = binding.edtUrl
        btnGoto = binding.btnGoto
    }

    private fun addOnClickListener() {
        btnGoto.setOnClickListener {
            if (edtUrl.text.trim().isNotEmpty()) {
                if (isValidUrl(edtUrl.text.toString())) {
                    try {
                        val uri = Uri.parse(getString(R.string.http) + edtUrl.text.toString())
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)

                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(this, R.string.app_not_found, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, R.string.invalid_url, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.enter_url, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidUrl(url: String): Boolean {
        val urlRegex =(getString(R.string.http_https_www_a_za_z0_9_2_256_a_z_2_6_b_a_za_z0_9))
        return url.matches(urlRegex.toRegex())
    }
}