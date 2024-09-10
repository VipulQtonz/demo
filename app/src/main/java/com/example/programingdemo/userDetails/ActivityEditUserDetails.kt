package com.example.programingdemo.userDetails

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityEditUserDetailsBinding
import com.example.programingdemo.utlis.Const.ADDRESS
import com.example.programingdemo.utlis.Const.CITY
import com.example.programingdemo.utlis.Const.EMAIL
import com.example.programingdemo.utlis.Const.FEMALE
import com.example.programingdemo.utlis.Const.FIRSTNAME
import com.example.programingdemo.utlis.Const.LASTNAME
import com.example.programingdemo.utlis.Const.HOBBIES
import com.example.programingdemo.utlis.Const.GENDER
import com.example.programingdemo.utlis.Const.IMAGE
import com.example.programingdemo.utlis.Const.IMAGE_URI
import com.example.programingdemo.utlis.Const.MALE
import com.example.programingdemo.utlis.Const.STATE
import com.example.programingdemo.utlis.Const.MOBILE_NUMBER
import com.example.programingdemo.utlis.Const.OTHER
import com.example.programingdemo.utlis.Const.SHARED_PREF_USER_DETAILS
import com.example.programingdemo.utlis.SharedPreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ActivityEditUserDetails : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {
    private lateinit var binding: ActivityEditUserDetailsBinding
    private val stateCodes = arrayOf(
        "Tap to Select State",
        "Andhra Pradesh",
        "Arunachal Pradesh",
        "Assam",
        "Bihar",
        "Chhattisgarh",
        "Goa",
        "Gujarat",
        "Haryana",
        "Himachal Pradesh",
        "Jharkhand",
        "Karnataka",
        "Kerala",
        "Madhya Pradesh",
        "Maharashtra",
        "Manipur",
        "Meghalaya",
        "Mizoram",
        "Nagaland",
        "Odisha",
        "Punjab",
        "Rajasthan",
        "Sikkim",
        "Tamil Nadu",
        "Telangana",
        "Tripura",
        "Uttar Pradesh",
        "Uttarakhand",
        "West Bengal"
    )
    private val gujaratCities = arrayOf(
        "Tap to Select City",
        "Ahmedabad",
        "Surat",
        "Vadodara",
        "Rajkot",
        "Bhavnagar",
        "Jamnagar",
        "Junagadh",
        "Gandhinagar",
        "Anand",
        "Nadiad",
        "Morbi",
        "Mehsana",
        "Bhuj",
        "Porbandar"
    )
    private lateinit var selectedState: String
    private lateinit var selectedCity: String
    private lateinit var selectedHobbies: List<String>
    private var selectedImageBitmap: Bitmap? = null
    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.EditUserProfileMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        addOnClickListener()
    }

    private fun init() {
        initStateSpinner()
        initCitySpinner()
    }

    private fun addOnClickListener() {
        binding.btnCancel.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.btnBrowse.setOnClickListener(this)
        binding.llMale.setOnClickListener(this)
        binding.llFemale.setOnClickListener(this)
        binding.llOther.setOnClickListener(this)
    }

    private fun initCitySpinner() {
        spinnerInit(binding.spCity, gujaratCities, R.string.tap_to_select_city.toString())
    }

    private fun initStateSpinner() {
        spinnerInit(binding.spState, stateCodes, R.string.tap_to_select_state.toString())
    }

    private fun spinnerInit(spinner: Spinner, array: Array<String>, defaultValue: String) {
        spinner.onItemSelectedListener = this

        val adapter: ArrayAdapter<CharSequence> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, array)

        adapter.setDropDownViewResource(R.layout.simple_list_item_single_choice_one)
        spinner.adapter = adapter
        val spinnerPositionState: Int = adapter.getPosition(defaultValue)
        spinner.setSelection(spinnerPositionState)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            R.id.spState -> {
                selectedState = stateCodes[p2]
                binding.tvStateError.visibility = View.GONE
            }

            R.id.spCity -> {
                selectedCity = gujaratCities[p2]
                binding.tvCityError.visibility = View.GONE
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        when (p0?.id) {
            R.id.spState -> {
                binding.tvStateError.visibility = View.VISIBLE
            }

            R.id.spCity -> {
                binding.tvCityError.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnCancel -> {
                clearAllFields()
            }

            R.id.btnSubmit -> {
                valideAndSubmit()
            }

            R.id.btnBrowse -> {
                openGallery()
            }

            R.id.llMale -> {
                checkOnlyMale()
            }

            R.id.llFemale -> {
                checkOnlyFemale()
            }

            R.id.llOther -> {
                checkOnlyOther()
            }
        }
    }

    private fun checkOnlyMale() {
        binding.imgMale.setImageResource(R.drawable.fill_radio_button)
        binding.imgFemale.setImageResource(R.drawable.un_fill_radio_button)
        binding.imgOther.setImageResource(R.drawable.un_fill_radio_button)
        selectedGender = MALE
    }

    private fun checkOnlyFemale() {
        binding.imgMale.setImageResource(R.drawable.un_fill_radio_button)
        binding.imgFemale.setImageResource(R.drawable.fill_radio_button)
        binding.imgOther.setImageResource(R.drawable.un_fill_radio_button)
        selectedGender = FEMALE
    }

    private fun checkOnlyOther() {
        binding.imgMale.setImageResource(R.drawable.un_fill_radio_button)
        binding.imgFemale.setImageResource(R.drawable.un_fill_radio_button)
        binding.imgOther.setImageResource(R.drawable.fill_radio_button)
        selectedGender = OTHER
    }

    private fun valideAndSubmit() {
        if (validation()) {
            passData()
        } else {
            Toast.makeText(
                this@ActivityEditUserDetails, R.string.all_values, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun clearAllFields() {
        binding.edtFirstName.text.clear()
        binding.edtLastName.text.clear()
        binding.edtEmail.text.clear()
        binding.edtMobileNumber.text.clear()
        binding.edtAdress.text.clear()
        binding.spState.setSelection(0)
        binding.spCity.setSelection(0)
        clearGenderSelection()
        clearCheckBoxes()
        clearSelectedImage()
    }

    private fun clearGenderSelection() {
        selectedGender = null
        binding.imgMale.setImageResource(R.drawable.un_fill_radio_button)
        binding.imgFemale.setImageResource(R.drawable.un_fill_radio_button)
        binding.imgOther.setImageResource(R.drawable.un_fill_radio_button)
    }

    private fun passData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                compressAndSubmitImage()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ActivityEditUserDetails, R.string.small_size_error, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun clearCheckBoxes() {
        binding.cbSport.isChecked = false
        binding.cbReading.isChecked = false
        binding.cbMusic.isChecked = false
        binding.cbDrawing.isChecked = false
        binding.cbPainting.isChecked = false
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

        if (binding.edtAdress.text.trim().isEmpty()) {
            binding.edtAdress.error = getString(R.string.enter_address)
            isValid = false
        }

        if (!validateSelection(binding.spCity)) {
            binding.tvCityError.visibility = View.VISIBLE
            isValid = false
        }

        if (!validateSelection(binding.spState)) {
            binding.tvStateError.visibility = View.VISIBLE
            isValid = false
        }

        if (selectedImageBitmap == null) {
            binding.tvImageError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvImageError.visibility = View.GONE
        }

        if (selectedGender == null) {
            binding.rdGroupError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.rdGroupError.visibility = View.GONE
        }

        if (!validateCheckBoxes(
                binding.cbSport,
                binding.cbReading,
                binding.cbMusic,
                binding.cbDrawing,
                binding.cbPainting
            )
        ) {
            binding.tvCheckBoxError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvCheckBoxError.visibility = View.GONE
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

    private fun validateSelection(spinner: Spinner): Boolean {
        return spinner.selectedItemPosition != 0
    }

    private fun clearSelectedImage() {
        selectedImageBitmap = null
    }

    private fun validateCheckBoxes(
        sport: CheckBox, reading: CheckBox, music: CheckBox, drawing: CheckBox, painting: CheckBox
    ): Boolean {

        if (!sport.isChecked && !reading.isChecked && !music.isChecked && !drawing.isChecked && !painting.isChecked) {
            binding.tvCheckBoxError.visibility = View.VISIBLE
            return false
        } else {
            binding.tvCheckBoxError.visibility = View.GONE
            getSelectedOptions(
                binding.cbSport,
                binding.cbReading,
                binding.cbMusic,
                binding.cbDrawing,
                binding.cbPainting
            )
            return true
        }
    }

    private suspend fun compressAndSubmitImage() {
        val bundle = Bundle().apply {
            putString(FIRSTNAME, binding.edtFirstName.text.trim().toString())
            putString(LASTNAME, binding.edtLastName.text.trim().toString())
            putString(EMAIL, binding.edtEmail.text.trim().toString())
            putString(MOBILE_NUMBER, binding.edtMobileNumber.text.trim().toString())
            putString(GENDER, selectedGender)
            putStringArrayList(HOBBIES, ArrayList(selectedHobbies))
            putString(ADDRESS, binding.edtAdress.text.trim().toString())
            putString(STATE, selectedState)
            putString(CITY, selectedCity)
        }

        val stream = ByteArrayOutputStream()
        selectedImageBitmap?.compress(
            Bitmap.CompressFormat.JPEG, 80, stream
        )

        val file = File(cacheDir, "image.png")
        withContext(Dispatchers.IO) {
            FileOutputStream(file).use { out ->
                selectedImageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        }


        //for storing sharePreferences data
        val sharedPreferencesHelper =
            SharedPreferencesHelper(this, SHARED_PREF_USER_DETAILS, MODE_PRIVATE)

        sharedPreferencesHelper.putString(FIRSTNAME, binding.edtFirstName.text.trim().toString())
        sharedPreferencesHelper.putString(LASTNAME, binding.edtLastName.text.trim().toString())
        sharedPreferencesHelper.putString(EMAIL, binding.edtEmail.text.trim().toString())
        sharedPreferencesHelper.putString(
            MOBILE_NUMBER, binding.edtMobileNumber.text.trim().toString()
        )
        sharedPreferencesHelper.putString(GENDER, selectedGender?.trim().toString())
        sharedPreferencesHelper.putString(HOBBIES, stringArrayToString(selectedHobbies))
        sharedPreferencesHelper.putString(ADDRESS, binding.edtAdress.text.trim().toString())
        sharedPreferencesHelper.putString(STATE, selectedState)
        sharedPreferencesHelper.putString(CITY, selectedCity)
        sharedPreferencesHelper.putString(IMAGE, file.toURI().toString())
        //for storing sharePreferences data


        bundle.putString(IMAGE_URI, file.toURI().toString())
        withContext(Dispatchers.IO) {
            val intent =
                Intent(this@ActivityEditUserDetails, ActivityUserDetailsPreview::class.java)
//            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun getSelectedOptions(
        sport: CheckBox, reading: CheckBox, music: CheckBox, drawing: CheckBox, painting: CheckBox
    ) {
        val selectedOptions = mutableListOf<String>()
        if (sport.isChecked) {
            selectedOptions.add(sport.text.toString())
        }
        if (reading.isChecked) {
            selectedOptions.add(reading.text.toString())
        }
        if (music.isChecked) {
            selectedOptions.add(music.text.toString())
        }
        if (drawing.isChecked) {
            selectedOptions.add(drawing.text.toString())
        }
        if (painting.isChecked) {
            selectedOptions.add(painting.text.toString())
        }
        selectedHobbies = selectedOptions
    }

    private val getImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Glide.with(this).asBitmap().load(uri).into(object : CustomTarget<Bitmap>() {

                    override fun onResourceReady(
                        resource: Bitmap, transition: Transition<in Bitmap>?
                    ) {
                        selectedImageBitmap = resource
                        binding.tvImageError.visibility = View.GONE
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
            }
        }

    private fun openGallery() {
        getImageResult.launch("image/*")
    }

    private fun stringArrayToString(array: List<String>): String {
        return array.joinToString(",")
    }
}