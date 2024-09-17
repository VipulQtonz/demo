package com.example.programingdemo.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityFirebaseFirestoreBinding

class ActivityFirebaseFirestore : AppCompatActivity() {
    private lateinit var binding: ActivityFirebaseFirestoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFirebaseFirestoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityFirebaseFirestoreMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val newUserProfile = UserProfileInfo(
//            id = "new",
//            name = Name("vipul", "jadav"),
//            email = "vipuljadav542@gmail.com",
//            address = Address("Rajkot", "Gujarat", "360023"),
//            dateOfBirth = "15-01-2003"
//        )
//
//        firestore.collection("UserData")
//            .add(newUserProfile)
//            .addOnSuccessListener {
//            }
//            .addOnFailureListener { e ->
//            }
    }
}