package com.example.programingdemo.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.MyApp.Companion.firebaseAuth
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityMobileNumberVarificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ActivityMobileNumberVarification : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMobileNumberVarificationBinding
    private lateinit var etPhoneNumber: EditText
    private lateinit var etOtp: EditText
    private lateinit var btnSendOtp: Button
    private lateinit var btnVerifyOtp: Button
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMobileNumberVarificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.llActivityMobileNumberVarificationMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        addOnclickListener()
    }

    private fun addOnclickListener() {
        btnVerifyOtp.setOnClickListener(this)
        btnSendOtp.setOnClickListener(this)
    }

    private fun init() {
        etPhoneNumber = binding.edtPhoneNumber
        etOtp = binding.edtOtp
        btnSendOtp = binding.btnSendOtp
        btnVerifyOtp = binding.btnVerifyOtp
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(
                applicationContext,
                getString(R.string.verification_failed, e.message),
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            this@ActivityMobileNumberVarification.verificationId = verificationId
            etOtp.visibility = View.VISIBLE
            btnVerifyOtp.visibility = View.VISIBLE
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.verification_successful),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.verification_failed_, task.exception?.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnSendOtp -> {
                val phoneNumber = etPhoneNumber.text.toString()
                sendVerificationCode(phoneNumber)
            }

            R.id.btnVerifyOtp -> {
                val code = etOtp.text.toString()
                verifyCode(code)
            }
        }

    }
}