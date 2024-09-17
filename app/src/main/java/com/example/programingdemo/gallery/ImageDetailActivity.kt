package com.example.programingdemo.gallery

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityImageDetailBinding
import com.example.programingdemo.utlis.Const.IMAGE_PATH

class ImageDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityImageDetailMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imageView = binding.imgFullImage
        val imagePath = intent.getStringExtra(IMAGE_PATH)
        if (imagePath != null) {
            Glide.with(this)
                .load(imagePath)
                .into(imageView)
        }
    }
}