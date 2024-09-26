@file:Suppress("DEPRECATION")

package com.example.programingdemo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.MyApp.Companion.firebaseAuth
import com.example.programingdemo.R
import com.example.programingdemo.data.UserData
import com.example.programingdemo.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivityLogin : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var displayNameEditText: EditText
    private lateinit var signUpButton: ImageView
    private lateinit var db: FirebaseFirestore
    private lateinit var loginWithGoogle: LinearLayout
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LoginActivityMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkCurrentUser()
        init()
        setupGoogleSignIn()
        addOnClickListener()
    }

    private fun addOnClickListener() {
        signUpButton.setOnClickListener(this)
        loginWithGoogle.setOnClickListener(this)
    }

    private fun init() {
        db = Firebase.firestore
        emailEditText = binding.edtEmail
        passwordEditText = binding.edtPassword
        displayNameEditText = binding.edtDisplayName
        signUpButton = binding.btnSubmit
        loginWithGoogle = binding.btnLoginWithGoogle
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Toast.makeText(
                this, getString(R.string.google_sign_in_failed_, e.message), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this, ActivityChat::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.authentication_failed, task.exception?.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun registerEmailAndPassword() {
        val email = emailEditText.text.trim().toString()
        val password = passwordEditText.text.trim().toString()
        val displayName = displayNameEditText.text.trim().toString()

        if (email.isNotEmpty() && password.isNotEmpty() && displayName.isNotEmpty()) {
            signUpWithEmailPassword(email, password, displayName)
        } else {
            Toast.makeText(
                this@ActivityLogin,
                getString(R.string.please_enter_email_and_password),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun signUpWithEmailPassword(email: String, password: String, displayName: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    clearEditTextFields()
                    Toast.makeText(
                        this, getString(R.string.registration_success), Toast.LENGTH_SHORT
                    ).show()
                    addDataToFireStore(
                        UserData(
                            userId = firebaseAuth.currentUser!!.uid,
                            email = email,
                            password = password,
                            userName = displayName
                        )
                    )
                    startActivity(Intent(this, ActivityChat::class.java))
                    finish()
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        signInWithEmailPassword(email, password)
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.registration_failed, task.exception?.message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun addDataToFireStore(userMessage: UserData) {
        db.collection("ChatUser").document(userMessage.userId).set(userMessage)
            .addOnSuccessListener {
                Toast.makeText(
                    this, getString(
                        R.string.user_added_successfully_with_id
                    ), Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this, getString(R.string.failed_to_add_user), Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun signInWithEmailPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, ActivityChat::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.authentication_failed, task.exception?.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun clearEditTextFields() {
        binding.edtEmail.text.clear()
        binding.edtPassword.text.clear()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnSubmit -> {
                registerEmailAndPassword()
            }

            R.id.btnLoginWithGoogle -> {
                signInWithGoogle()
            }
        }
    }

    private fun checkCurrentUser() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                startActivity(Intent(this, ActivityChat::class.java))
                finish()
            }
        }
    }
}
