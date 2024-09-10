package com.example.programingdemo.userDetails

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.FIRSTNAME
import com.example.programingdemo.databinding.ActivityUserDetailsPreviewBinding
import com.example.programingdemo.utlis.Const.ADDRESS
import com.example.programingdemo.utlis.Const.CITY
import com.example.programingdemo.utlis.Const.EMAIL
import com.example.programingdemo.utlis.Const.LASTNAME
import com.example.programingdemo.utlis.Const.HOBBIES
import com.example.programingdemo.utlis.Const.GENDER
import com.example.programingdemo.utlis.Const.IMAGE
import com.example.programingdemo.utlis.Const.IMAGE_URI
import com.example.programingdemo.utlis.Const.STATE
import com.example.programingdemo.utlis.Const.MOBILE_NUMBER
import com.example.programingdemo.utlis.Const.SHARED_PREF_USER_DETAILS
import com.example.programingdemo.utlis.SharedPreferencesHelper
import java.io.File
import java.io.FileInputStream

class ActivityUserDetailsPreview : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailsPreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserDetailsPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.UserProfilePreviewMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        getIntentData()
        getSharedPreferencesData()
        addOnClickListener()

    }

    private fun addOnClickListener() {
        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun getIntentData() {
        val bundle = intent?.extras
        val firstName = bundle?.getString(FIRSTNAME)
        val lastName = bundle?.getString(LASTNAME)
        val email = bundle?.getString(EMAIL)
        val mobileNumber = bundle?.getString(MOBILE_NUMBER)
        val gender = bundle?.getString(GENDER)
        val hobbies = bundle?.getStringArrayList(HOBBIES)
        val address = bundle?.getString(ADDRESS)
        val state = bundle?.getString(STATE)
        val city = bundle?.getString(CITY)
        val imageUriString = bundle?.getString(IMAGE_URI)
        val imageUri = imageUriString?.let { Uri.parse(it) }

        if (firstName!!.isNotEmpty() && lastName!!.isNotEmpty() && email!!.isNotEmpty() && mobileNumber!!.isNotEmpty() && gender!!.isNotEmpty() && hobbies!!.isNotEmpty() && address!!.isNotEmpty() && state!!.isNotEmpty() && city!!.isNotEmpty() && imageUri != null) {
            val bitmap = imageUriToBitmap(imageUri)
            if (bitmap != null) {
                setData(
                    firstName,
                    lastName,
                    email,
                    mobileNumber,
                    gender,
                    hobbies.toString(),
                    address,
                    state,
                    city,
                    bitmap
                )
            }
        } else {
            Toast.makeText(
                this@ActivityUserDetailsPreview, R.string.something_went_wrong, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun imageUriToBitmap(uri: Uri): Bitmap? {
        return try {
            val file = File(uri.path!!)
            BitmapFactory.decodeStream(FileInputStream(file))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun setData(
        firstName: String,
        lastName: String,
        email: String,
        mobileNumber: String,
        gender: String,
//        hobbies: List<String>,
        hobbies: String,
        address: String,
        state: String,
        city: String,
        image: Bitmap
    ) {
        binding.tvFirstName.text = firstName
        binding.tvLastName.text = lastName
        binding.tvEmail.text = email
        binding.tvMobileNumber.text = mobileNumber
        binding.tvGender.text = gender
//        binding.tvHobby.text = hobbies.joinToString(", ")
        binding.tvHobby.text = hobbies
        binding.tvAddress.text = address
        binding.tvState.text = state
        binding.tvCity.text = city
        binding.imgProfilePicture.setImageBitmap(image)
    }

    private fun getSharedPreferencesData() {
        val sharedPreferencesHelper =
            SharedPreferencesHelper(this, SHARED_PREF_USER_DETAILS, MODE_PRIVATE)
        val firstName = sharedPreferencesHelper.getString(FIRSTNAME, null)
        val lastName = sharedPreferencesHelper.getString(LASTNAME, null)
        val email = sharedPreferencesHelper.getString(EMAIL, null)
        val mobileNumber = sharedPreferencesHelper.getString(MOBILE_NUMBER, null)
        val address = sharedPreferencesHelper.getString(ADDRESS, null)
        val gender = sharedPreferencesHelper.getString(GENDER, null)
        val hobbies = sharedPreferencesHelper.getString(HOBBIES, null)
        val state = sharedPreferencesHelper.getString(STATE, null)
        val city = sharedPreferencesHelper.getString(CITY, null)
        val image = sharedPreferencesHelper.getString(IMAGE, null)
        val imageUri = image?.let { Uri.parse(it) }

        if (firstName != null && lastName != null && email != null && mobileNumber != null && address != null && gender != null && hobbies != null && state != null && city != null && imageUri != null) {
            val bitmap = imageUriToBitmap(imageUri)
            if (bitmap != null) {
                setData(
                    firstName,
                    lastName,
                    email,
                    mobileNumber,
                    gender,
                    hobbies,
                    address,
                    state,
                    city,
                    bitmap
                )
            }
        } else {
            Toast.makeText(this, R.string.data_not_found, Toast.LENGTH_SHORT).show()
        }
    }
}