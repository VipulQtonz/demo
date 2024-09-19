package com.example.programingdemo.realTimeDatabase

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityFirestoreDatabaseBinding
import com.example.programingdemo.utlis.Const.ID
import com.example.programingdemo.utlis.Const.SAVE
import com.example.programingdemo.utlis.Const.UPDATE
import com.example.programingdemo.utlis.Const.USER_DATABASE_
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.Calendar

class ActivityFirestoreDatabase : AppCompatActivity(),
    FirestoreDatabaseAdapter.OnItemClickListener {
    private lateinit var binding: ActivityFirestoreDatabaseBinding
    private lateinit var db: FirebaseFirestore
    private val _userProfileData = MutableLiveData<List<UserProfileInfo>>()
    private val userProfileData: LiveData<List<UserProfileInfo>> get() = _userProfileData
    private lateinit var adapter: FirestoreDatabaseAdapter
    private var updateId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFirestoreDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityFirestoreDatabaseMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        addOnClickListener()
        observeData()
    }

    private fun init() {
        db = Firebase.firestore
        adapter = FirestoreDatabaseAdapter(emptyList(), this@ActivityFirestoreDatabase)
        binding.rwMain.adapter = adapter
        binding.rwMain.layoutManager = LinearLayoutManager(this@ActivityFirestoreDatabase)
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

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && validateEmail() && city.isNotEmpty() && state.isNotEmpty() && pinCode.isNotEmpty() && dateOfBirth.isNotEmpty()) {
                val userProfile = UserProfileInfo(
                    id = updateId ?: "",
                    name = Name(firstName, lastName),
                    email = email,
                    address = Address(city, state, pinCode),
                    dateOfBirth = dateOfBirth
                )
                if (updateId != null) {
                    db.collection(USER_DATABASE_).document(updateId!!).set(userProfile)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                getString(R.string.user_updated_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            resetUpdateState()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this, getString(R.string.failed_to_update_user), Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    db.collection(USER_DATABASE_).add(userProfile)
                        .addOnSuccessListener { documentReference ->
                            val generatedId = documentReference.id
                            documentReference.update(ID, generatedId).addOnSuccessListener {
                                Toast.makeText(
                                    this, getString(
                                        R.string.user_added_successfully_with_id, generatedId
                                    ), Toast.LENGTH_SHORT
                                ).show()
                                resetUpdateState()
                            }.addOnFailureListener {
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                this, getString(R.string.failed_to_add_user), Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            } else {
                Toast.makeText(this, getString(R.string.please_enter_data), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun observeData() {
        db.collection(USER_DATABASE_)
//            .orderBy("age")
//            .limit(10)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }

                val dataList = mutableListOf<UserProfileInfo>()
                snapshot?.let { it ->
                    for (document in it) {
                        val userProfile = document.toObject(UserProfileInfo::class.java)
                        userProfile.let { dataList.add(it) }
                    }
                    _userProfileData.value = dataList
                }
            }
        userProfileData.observe(this) { dataList ->
            binding.pbMain.visibility = View.GONE
            adapter.updateData(dataList)
        }
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

    override fun onDeleteClick(item: UserProfileInfo) {
        db.collection(USER_DATABASE_).document(item.id).delete().addOnSuccessListener {
            Toast.makeText(
                this, getString(R.string.user_deleted_successfully), Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener { e ->
            Toast.makeText(this, getString(R.string.failed_to_delete_user), Toast.LENGTH_SHORT)
                .show()
        }
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
            this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val formattedDate = "${selectedDayOfMonth}/${selectedMonth + 1}/${selectedYear}"
                binding.edtDateOfBirth.setText(formattedDate)
            }, year, month, day
        )
        datePickerDialog.show()
    }
}