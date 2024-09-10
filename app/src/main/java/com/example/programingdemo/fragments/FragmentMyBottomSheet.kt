package com.example.programingdemo.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.programingdemo.R
import com.example.programingdemo.databinding.FragmentMyBottomSheetBinding
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class FragmentMyBottomSheet : BottomSheetDialogFragment(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {
    private lateinit var binding: FragmentMyBottomSheetBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialog = dialog as BottomSheetDialog
        val behavior = bottomSheetDialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

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
        spinnerInit(binding.spCity, gujaratCities, getString(R.string.tap_to_select_city))
    }

    private fun initStateSpinner() {
        spinnerInit(binding.spState, stateCodes, getString(R.string.tap_to_select_state))
    }

    private fun spinnerInit(spinner: Spinner, array: Array<String>, defaultValue: String) {
        spinner.onItemSelectedListener = this

        val adapter: ArrayAdapter<CharSequence> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, array)

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
                validateAndSubmit()
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

    private fun validateAndSubmit() {
        if (validation()) {
            passData()
        } else {
            Toast.makeText(
                requireContext(), R.string.all_values, Toast.LENGTH_SHORT
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
                        requireContext(), R.string.small_size_error, Toast.LENGTH_SHORT
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
        }

        if (binding.edtMobileNumber.text.trim().isEmpty()) {
            binding.edtMobileNumber.error = getString(R.string.enter_mobile_number)
            isValid = false
        }

        if (binding.edtAdress.text.trim().isEmpty()) {
            binding.edtAdress.error = getString(R.string.enter_address)
            isValid = false
        }

        if (selectedState == stateCodes[0]) {
            binding.tvStateError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvStateError.visibility = View.GONE
        }

        if (selectedCity == gujaratCities[0]) {
            binding.tvCityError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvCityError.visibility = View.GONE
        }

        if (selectedGender == null) {
            Toast.makeText(requireContext(), R.string.select_gender, Toast.LENGTH_SHORT).show()
            isValid = false
        }
        return isValid
    }

    private fun compressAndSubmitImage() {
        selectedImageBitmap?.let { bitmap ->
            val compressedImageFile = File(requireContext().cacheDir, "compressed_image.jpg")
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            val imageBytes = outputStream.toByteArray()

            FileOutputStream(compressedImageFile).use { fos ->
                fos.write(imageBytes)
            }

            val sharedPreferences = SharedPreferencesHelper(
                requireContext(), SHARED_PREF_USER_DETAILS, Context.MODE_PRIVATE
            )
            sharedPreferences.putString(IMAGE, compressedImageFile.absolutePath)
            submitData()
        } ?: run {
            submitData()
        }
    }

    private fun submitData() {
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val email = binding.edtEmail.text.toString()
        val mobileNumber = binding.edtMobileNumber.text.toString()
        val address = binding.edtAdress.text.toString()
        val hobbies = getSelectedHobbies()
        val data = mapOf(
            FIRSTNAME to firstName,
            LASTNAME to lastName,
            EMAIL to email,
            MOBILE_NUMBER to mobileNumber,
            ADDRESS to address,
            STATE to selectedState,
            CITY to selectedCity,
            GENDER to selectedGender,
            HOBBIES to hobbies
        )

        Toast.makeText(requireContext(), R.string.data_saved_successfully, Toast.LENGTH_SHORT)
            .show()
    }

    private fun getSelectedHobbies() {
        val hobbies = mutableListOf<String>()
        if (binding.cbSport.isChecked) {
            hobbies.add(binding.cbSport.text.toString())
        }
        if (binding.cbReading.isChecked) {
            hobbies.add(binding.cbReading.text.toString())
        }
        if (binding.cbMusic.isChecked) {
            hobbies.add(binding.cbMusic.text.toString())
        }
        if (binding.cbDrawing.isChecked) {
            hobbies.add(binding.cbDrawing.text.toString())
        }
        if (binding.cbPainting.isChecked) {
            hobbies.add(binding.cbPainting.text.toString())
        }
        selectedHobbies = hobbies
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    Glide.with(this).asBitmap().load(it).into(object : CustomTarget<Bitmap>() {
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
        }

    private fun clearSelectedImage() {
        selectedImageBitmap = null
    }
}
