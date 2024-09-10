package com.example.programingdemo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityServiceDemoBinding
import com.example.programingdemo.services.ServiceIntent
import com.example.programingdemo.utlis.GeneralUsage

class ActivityServiceDemo : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityServiceDemoBinding
    private lateinit var generalUsage: GeneralUsage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityServiceDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ServiceDemoMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        generalUsage = GeneralUsage(this)
        addOnClickListener()
    }

    private fun addOnClickListener() {
        binding.btnStopButton.setOnClickListener(this)
        binding.btnStartButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnStartButton -> {
                if (!generalUsage.isServiceRunning(ServiceIntent::class.java)) {
                    startService(Intent(this@ActivityServiceDemo, ServiceIntent::class.java))
                } else {
                    Toast.makeText(this, R.string.service_running_all_ready, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            R.id.btnStopButton -> {
                stopService(Intent(this@ActivityServiceDemo, ServiceIntent::class.java))
            }
        }
    }
}