package com.example.programingdemo.intent

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityExplicitIntentDemoOneBinding
import com.example.programingdemo.utlis.Const.ADDRESS
import com.example.programingdemo.utlis.Const.EMAIL
import com.example.programingdemo.utlis.Const.FIRSTNAME
import com.example.programingdemo.utlis.Const.LASTNAME
import com.example.programingdemo.utlis.Const.MOBILE_NUMBER

class ActivityExplicitIntentDemoOne : AppCompatActivity() {
    private lateinit var binding: ActivityExplicitIntentDemoOneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExplicitIntentDemoOneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityExplicitIntentDemoOneMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }

        addOnClickListener()
    }

    private fun addOnClickListener() {
        binding.btnSendData.setOnClickListener {
            if (validation()) {
                val bundle = Bundle()
                bundle.putString(FIRSTNAME, binding.edtFirstName.text.toString())
                bundle.putString(LASTNAME, binding.edtLastName.text.toString())
                bundle.putString(EMAIL, binding.edtEmail.text.toString())
                bundle.putString(MOBILE_NUMBER, binding.edtMobileNumber.text.toString())
                bundle.putString(ADDRESS, binding.edtAddress.text.toString())
                val intent = Intent(
                    this@ActivityExplicitIntentDemoOne,
                    ActivityRecieveDataFromExplicitEntent::class.java
                )
                intent.putExtras(bundle)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@ActivityExplicitIntentDemoOne, R.string.all_values, Toast.LENGTH_SHORT
                ).show()
            }
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
            binding.edtMobileNumber.error =
                getString(R.string.enter_different_digits)
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