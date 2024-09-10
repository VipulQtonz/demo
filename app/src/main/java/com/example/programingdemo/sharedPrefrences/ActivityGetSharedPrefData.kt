package com.example.programingdemo.sharedPrefrences

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityGetSharedPrefDataBinding
import com.example.programingdemo.utlis.Const.ADDRESS
import com.example.programingdemo.utlis.Const.EMAIL
import com.example.programingdemo.utlis.Const.FIRSTNAME
import com.example.programingdemo.utlis.Const.LASTNAME
import com.example.programingdemo.utlis.Const.MOBILE_NUMBER
import com.example.programingdemo.utlis.Const.SHARED_PREF_USER_DETAILS
import com.example.programingdemo.utlis.SharedPreferencesHelper

class ActivityGetSharedPrefData : AppCompatActivity() {
    private lateinit var binding: ActivityGetSharedPrefDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGetSharedPrefDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.GetSharedPrefDataMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getSharedPreferencesData()
    }

    private fun getSharedPreferencesData() {
        val sharedPreferencesHelper =
            SharedPreferencesHelper(this, SHARED_PREF_USER_DETAILS, MODE_PRIVATE)
        val firstName = sharedPreferencesHelper.getString(FIRSTNAME, null)
        val lastName = sharedPreferencesHelper.getString(LASTNAME, null)
        val email = sharedPreferencesHelper.getString(EMAIL, null)
        val mobileNumber = sharedPreferencesHelper.getString(MOBILE_NUMBER, null)
        val address = sharedPreferencesHelper.getString(ADDRESS, null)

        if (firstName != null && lastName != null && email != null && mobileNumber != null && address != null) {
            binding.tvFirstName.text = firstName
            binding.tvLastName.text = lastName
            binding.tvEmail.text = email
            binding.tvMobileNumber.text = mobileNumber
            binding.tvAddress.text = address
        } else {
            Toast.makeText(this, R.string.data_not_found, Toast.LENGTH_SHORT).show()
        }
    }
}