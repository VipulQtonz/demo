package com.example.programingdemo.realTimeDatabase

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityRealTimeDatabaseBinding
import com.example.programingdemo.utlis.Const.SAVE
import com.example.programingdemo.utlis.Const.UPDATE
import java.util.Calendar

class ActivityRealTimeDatabase : AppCompatActivity(), RealtimeDatabaseAdapter.OnItemClickListener {
    private lateinit var binding: ActivityRealTimeDatabaseBinding
    private val viewModel: RealTimeDatabaseViewModel by viewModels()
    private lateinit var adapter: RealtimeDatabaseAdapter
    private var updateId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRealTimeDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ActivityRealTimeDatabaseMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        addOnClickListener()
        observeData()
    }

    private fun init() {
        binding.rwMain.layoutManager = LinearLayoutManager(this@ActivityRealTimeDatabase)
    }

    private fun addOnClickListener() {
        binding.imgSetDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnSave.setOnClickListener {
            val firstName = binding.edtFirstName.text.trim().toString()
            val lastName = binding.edtLastName.text.trim().toString()
            val email = binding.edtEmail.text.trim().toString()
            val city = binding.edtCity.text.trim().toString()
            val state = binding.edtState.text.trim().toString()
            val pinCode = binding.edtPincode.text.trim().toString()
            val dateOfBirth = binding.edtDateOfBirth.text.trim().toString()


            if (firstName.isNotEmpty() && lastName.isNotEmpty() && validateEmail() &&
                city.isNotEmpty() && state.isNotEmpty() && pinCode.isNotEmpty() && dateOfBirth.isNotEmpty()
            ) {
                if (updateId != null) {
                    viewModel.updateUserProfile(
                        UserProfileInfo(
                            id = updateId!!,
                            name = Name(firstName, lastName),
                            email = email,
                            address = Address(city, state, pinCode),
                            dateOfBirth = dateOfBirth
                        )
                    )
                    resetUpdateState()
                } else {
                    viewModel.addUserProfile(
                        UserProfileInfo(
                            id = "",
                            name = Name(firstName, lastName),
                            email = email,
                            address = Address(city, state, pinCode),
                            dateOfBirth = dateOfBirth
                        )
                    )
                    resetUpdateState()
                }
            } else {
                Toast.makeText(
                    this@ActivityRealTimeDatabase,
                    getString(R.string.please_enter_data), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeData() {
        viewModel.userProfileInfoList.observe(this) { dataList ->
            showValue(dataList)
        }
    }

    private fun showValue(dataList: List<UserProfileInfo>) {
        binding.pbMain.visibility = View.GONE
        adapter = RealtimeDatabaseAdapter(dataList, this@ActivityRealTimeDatabase)
        binding.rwMain.adapter = adapter
    }

    private fun validateEmail(): Boolean {
        var isValid = true
        if (binding.edtEmail.text.trim().isEmpty()) {
            binding.edtEmail.error = getString(R.string.enter_email)
            isValid = false
        } else if (!isValidEmail(binding.edtEmail.text.trim().toString())) {
            binding.edtEmail.error = getString(R.string.enter_valid_email_address)
            isValid = false
        }
        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(Regex(emailPattern))
    }

    private fun resetUpdateState() {
        binding.btnSave.text = SAVE
        binding.edtFirstName.text.clear()
        binding.edtFirstName.text.clear()
        binding.edtLastName.text.clear()
        binding.edtEmail.text.clear()
        binding.edtDateOfBirth.text.clear()
        binding.edtCity.text.clear()
        binding.edtState.text.clear()
        binding.edtPincode.text.clear()
        binding.btnSave.text = SAVE
        updateId = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }

    override fun onDeleteClick(item: UserProfileInfo) {
        viewModel.deleteUserProfile(item.id)
        resetUpdateState()
    }

    override fun onUpdateClick(item: UserProfileInfo) {
        updateId = item.id
        binding.edtFirstName.setText(item.name.firstName)
        binding.edtLastName.setText(item.name.middleName)
        binding.edtEmail.setText(item.email)
        binding.edtDateOfBirth.setText(item.dateOfBirth)
        binding.edtCity.setText(item.address.city)
        binding.edtState.setText(item.address.state)
        binding.edtPincode.setText(item.address.pinCode)
        binding.btnSave.text = UPDATE
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val formattedDate = "${selectedDayOfMonth}/${selectedMonth + 1}/${selectedYear}"
                binding.edtDateOfBirth.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
