package com.example.programingdemo.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.asyncTask.MyAsyncTask
import com.example.programingdemo.databinding.ActivityAsyncTaskDemoBinding

class ActivityAsyncTaskDemo : AppCompatActivity() {
    private lateinit var binding: ActivityAsyncTaskDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAsyncTaskDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ActivityAsyncTaskMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addOnClickListener()
    }

    private fun addOnClickListener() {
        binding.btnRefresh.setOnClickListener {
            val myAsyncTask = MyAsyncTask(
                this,
                binding.imgProfilePicture,
                binding.tvWait,
                binding.pbMain,
                binding.tvPositionError,
                binding.tvDeveloperNameError
            )
            myAsyncTask.execute()
        }
    }
}
