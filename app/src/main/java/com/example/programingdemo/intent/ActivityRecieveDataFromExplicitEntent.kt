package com.example.programingdemo.intent

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityRecieveDataFromExplicitEntentBinding
import com.example.programingdemo.utlis.Const.ADDRESS
import com.example.programingdemo.utlis.Const.FIRSTNAME
import com.example.programingdemo.utlis.Const.EMAIL
import com.example.programingdemo.utlis.Const.LASTNAME
import com.example.programingdemo.utlis.Const.MOBILE_NUMBER

class ActivityRecieveDataFromExplicitEntent : AppCompatActivity() {
    private lateinit var binding: ActivityRecieveDataFromExplicitEntentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecieveDataFromExplicitEntentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.llRecieveDataFromExplicitIntentMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }
        getIntentData()
        addOnClickListener()
    }

    private fun getIntentData() {
        val bundle = intent?.extras
        val firstName = bundle?.getString(FIRSTNAME)
        val lastName = bundle?.getString(LASTNAME)
        val email = bundle?.getString(EMAIL)
        val mobileNumber = bundle?.getString(MOBILE_NUMBER)
        val address = bundle?.getString(ADDRESS)

        if (firstName!!.isNotEmpty() && lastName!!.isNotEmpty() && email!!.isNotEmpty() && mobileNumber!!.isNotEmpty() && address!!.isNotEmpty()) {
            setData(firstName, lastName, email, mobileNumber, address)
        } else {
            Toast.makeText(
                this@ActivityRecieveDataFromExplicitEntent,
                R.string.values_error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(
        firstName: String?,
        lastName: String?,
        email: String?,
        mobileNumber: String?,
        address: String?
    ) {
        binding.tvFirstName.text = "$FIRSTNAME :$firstName"
        binding.tvLastName.text = "$LASTNAME :$lastName"
        binding.tvEmail.text = "$EMAIL :$email"
        binding.tvMobileNumber.text = "$MOBILE_NUMBER :$mobileNumber"
        binding.tvAddress.text = "$ADDRESS :$address"
    }

    private fun addOnClickListener() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}