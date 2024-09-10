package com.example.programingdemo.appFlowDemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityIntroScreenBinding
import com.example.programingdemo.userDetails.ActivityEditUserDetails
import com.example.programingdemo.utlis.Const.IS_OPEN
import com.example.programingdemo.utlis.Const.QTONZ_URI
import com.example.programingdemo.utlis.Const.SHARED_PREF_USER_DETAILS
import com.example.programingdemo.utlis.SharedPreferencesHelper

class ActivityIntroScreen : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityIntroScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIntroScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.llIntroScreenMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addOnClickListener()
    }

    private fun addOnClickListener() {
        binding.btnStart.setOnClickListener(this)
        binding.tvPrivacyPolicy.setOnClickListener(this)
        binding.tvTermsOfServices.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnStart -> {
                val sharedPreferencesHelper =
                    SharedPreferencesHelper(this, SHARED_PREF_USER_DETAILS, MODE_PRIVATE)
                sharedPreferencesHelper.putInt(IS_OPEN, 1)
                startActivity(Intent(this, ActivityEditUserDetails::class.java))
                finish()
            }

            R.id.tvPrivacyPolicy -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(QTONZ_URI)))
            }

            R.id.tvTermsOfServices -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(QTONZ_URI)))
            }
        }
    }
}