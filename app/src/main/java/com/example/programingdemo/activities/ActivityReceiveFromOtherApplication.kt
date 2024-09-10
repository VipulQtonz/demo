package com.example.programingdemo.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityReceiveFromOtherApplicationBinding
import com.example.programingdemo.utlis.Const.MESSAGE
import com.example.programingdemo.utlis.Const.SENDER_NAME

class ActivityReceiveFromOtherApplication : AppCompatActivity() {
    private lateinit var binding: ActivityReceiveFromOtherApplicationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReceiveFromOtherApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ReceivedFromOtherApplicationMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val senderName = intent?.getStringExtra(SENDER_NAME)
        val message = intent?.getStringExtra(MESSAGE)
        setData(senderName, message)

    }

    private fun setData(senderName: String?, message: String?) {
        if (!senderName.isNullOrEmpty() && !message.isNullOrEmpty()) {
            binding.tvSenderName.text = senderName
            binding.tvMessage.text = message
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val sender = intent.getStringExtra(SENDER_NAME)
        val message = intent.getStringExtra(MESSAGE)
        setData(sender, message)
    }
}