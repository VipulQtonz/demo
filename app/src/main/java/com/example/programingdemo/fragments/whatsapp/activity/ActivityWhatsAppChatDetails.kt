package com.example.programingdemo.fragments.whatsapp.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.programingdemo.R
import com.example.programingdemo.data.ChatItem
import com.example.programingdemo.databinding.ActivityWhatsAppChatDetailsBinding
import com.example.programingdemo.databinding.FragmentChatsBinding
import com.example.programingdemo.utlis.Const.CHAT_ITEM
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityWhatsAppChatDetails : AppCompatActivity() {
    private lateinit var binding: ActivityWhatsAppChatDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWhatsAppChatDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityWhatsAppChatMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        showOnlineUpdate()
    }

    private fun init() {
        getIntentData()
        binding.imgBackToChat.setOnClickListener {
            finish()
        }
    }

    private fun getIntentData() {
        val chatItem = intent.getSerializableExtra(CHAT_ITEM) as? ChatItem
        if (chatItem != null) {
            Glide.with(this)
                .load(chatItem.profileImageResId)
                .placeholder(R.drawable.user_profile)
                .error(R.drawable.user_profile)
                .into(binding.imgProfilePictureChat)
            binding.tvDisplayName.text = chatItem.displayName
        }
    }

    private fun showOnlineUpdate() {
        MainScope().launch {
            delay(3000)
            binding.tvLastSeen.visibility = View.VISIBLE
        }
    }
}