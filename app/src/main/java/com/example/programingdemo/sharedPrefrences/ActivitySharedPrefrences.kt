package com.example.programingdemo.sharedPrefrences

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.ADDRESS
import com.example.programingdemo.utlis.Const.FIRSTNAME
import com.example.programingdemo.utlis.Const.EMAIL
import com.example.programingdemo.utlis.Const.LASTNAME
import com.example.programingdemo.utlis.Const.MOBILE_NUMBER
import com.example.programingdemo.databinding.ActivitySharedPrefrencesBinding
import com.example.programingdemo.utlis.Const.SHARED_PREF_USER_DETAILS
import com.example.programingdemo.utlis.SharedPreferencesHelper

class ActivitySharedPrefrences : AppCompatActivity() {
    private lateinit var binding: ActivitySharedPrefrencesBinding
    private var areDetailsVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySharedPrefrencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.SharedPrefrencesMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addOnClickListener()
    }

    private fun addOnClickListener() {
        binding.btnSendData.setOnClickListener {
            if (validation()) {
                sendData()
                startActivity(
                    Intent(
                        this@ActivitySharedPrefrences,
                        ActivityGetSharedPrefData::class.java
                    )
                )
            }
        }
        binding.tvShowPrev.setOnClickListener {
            getSharedPreferencesData()
        }
    }

    private fun sendData() {
        val sharedPreferencesHelper =
            SharedPreferencesHelper(this, SHARED_PREF_USER_DETAILS, MODE_PRIVATE)
        sharedPreferencesHelper.putString(FIRSTNAME, binding.edtFirstName.text.toString())
        sharedPreferencesHelper.putString(LASTNAME, binding.edtLastName.text.toString())
        sharedPreferencesHelper.putString(EMAIL, binding.edtEmail.text.toString())
        sharedPreferencesHelper.putString(MOBILE_NUMBER, binding.edtMobileNumber.text.toString())
        sharedPreferencesHelper.putString(ADDRESS, binding.edtAddress.text.toString())
        Toast.makeText(
            this@ActivitySharedPrefrences,
            R.string.data_saved_successfully,
            Toast.LENGTH_SHORT
        )
            .show()
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
            if (areDetailsVisible) {
                binding.llFirstName.visibility = View.GONE
                binding.llLastName.visibility = View.GONE
                binding.llEmail.visibility = View.GONE
                binding.llMobileNumber.visibility = View.GONE
                binding.llAddress.visibility = View.GONE
            } else {
                binding.llFirstName.visibility = View.VISIBLE
                binding.llLastName.visibility = View.VISIBLE
                binding.llEmail.visibility = View.VISIBLE
                binding.llMobileNumber.visibility = View.VISIBLE
                binding.llAddress.visibility = View.VISIBLE

                binding.tvFirstNamePrev.text = firstName
                binding.tvLastNamePrev.text = lastName
                binding.tvEmailPrev.text = email
                binding.tvMobileNumberPrev.text = mobileNumber
                binding.tvAddressPrev.text = address
            }
            areDetailsVisible = !areDetailsVisible
        } else {
            Toast.makeText(this, R.string.data_not_found, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.edtFirstName.text.trim().isEmpty()) {
            binding.edtFirstName.error = getString(R.string.enter_firstname)
            isValid = false
        }

        if (binding.edtLastName.text.trim().isEmpty()) {
            binding.edtLastName.error = getString(R.string.enter_lastname)
            isValid = false
        }

        if (binding.edtEmail.text.trim().isEmpty()) {
            binding.edtEmail.error = getString(R.string.enter_email)
            isValid = false
        } else if (!isValidEmail(binding.edtEmail.text.trim().toString())) {
            binding.edtEmail.error = getString(R.string.enter_valid_email_address)
            isValid = false
        }

        if (binding.edtMobileNumber.text.trim().isEmpty()) {
            binding.edtMobileNumber.error = getString(R.string.enter_mobile_number)
            isValid = false
        } else if (binding.edtMobileNumber.text.trim().length != 10) {
            binding.edtMobileNumber.error = getString(R.string.enter_mobile_number)
            isValid = false
        } else if (isAllSameDigit(binding.edtMobileNumber.text.toString())) {
            binding.edtMobileNumber.error = getString(R.string.enter_different_digits)
            isValid = false
        }

        if (binding.edtAddress.text.trim().isEmpty()) {
            binding.edtAddress.error = getString(R.string.enter_address)
            isValid = false
        }
        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(Regex(emailPattern))
    }

    private fun isAllSameDigit(number: String): Boolean {
        return number.all { it == number.first() }
    }
}