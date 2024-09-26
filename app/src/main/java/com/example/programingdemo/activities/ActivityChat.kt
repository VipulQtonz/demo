package com.example.programingdemo.activities

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.MyApp.Companion.firebaseAuth
import com.example.programingdemo.MyApp.Companion.firebaseDatabase
import com.example.programingdemo.R
import com.example.programingdemo.adapters.ChatAdapter
import com.example.programingdemo.chatApplication.AccessToken
import com.example.programingdemo.chatApplication.api.Message
import com.example.programingdemo.chatApplication.api.Notification
import com.example.programingdemo.chatApplication.api.NotificationAPI
import com.example.programingdemo.chatApplication.api.NotificationContent
import com.example.programingdemo.data.UserMessage
import com.example.programingdemo.databinding.ActivityChatBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityChat : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var message: EditText
    private lateinit var sendButton: ImageView
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private var userName: String? = null
    private var messageList: MutableList<UserMessage> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clActivityChatMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        setupRecyclerView()
        findDisplayNameOfCurrentUserFromFirestore()
        loadMessagesFromFirebase()
        sendButton.setOnClickListener {
            sendNotification()
            if (message.text.trim().toString().isNotEmpty()) {
                val userMessage = UserMessage(
                    userId = firebaseAuth.currentUser!!.uid,
                    userName = userName.toString(),
                    message = message.text.trim().toString()
                )
                val data = firebaseDatabase.getReference("ChatData")
                data.push().setValue(userMessage).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        message.text.clear()
                    } else {
                        Log.e("Firebase", "Failed to add data", task.exception)
                    }
                }
            }
        }

        //FOR NOTIFICATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(applicationContext)
                .withPermissions(Manifest.permission.POST_NOTIFICATIONS)
                .withListener(object : PermissionListener, MultiplePermissionsListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                    }

                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()
                    }
                }).check()

        }
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
//        FirebaseMessaging.getInstance().subscribeToTopic("test")
    }

    private fun sendNotification() {

        val token =
            "eq4ZZcOTTyWVuF1cqQgeZn:APA91bHEwJ3QfaCDjSLFeSijTT2zTDSml99ev6r0OH4oxyZBUlDi7wsX102FO51z6W5v_PliDXsUbaxE4ySfxS8UccVqAJehwQkRxg1mB1jXR21gV2LlI007bJUZGdM-i__QFDiR7aq2"
        val notification = createNotification(token)
        val accessToken = AccessToken.getAccessToken()
        NotificationAPI.sendNotification().notification(notification, "Bearer $accessToken")
            .enqueue(
                object : Callback<Notification> {
                    override fun onResponse(
                        call: Call<Notification>,
                        response: Response<Notification>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ActivityChat,
                                "Notification sent successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.e(
                                "NotificationError",
                                "Failed to send notification: ${response.errorBody()?.string()}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<Notification>, t: Throwable) {
                        Toast.makeText(
                            this@ActivityChat,
                            "Failed to send notification",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("NotificationError", "Error: ${t.message}", t)
                    }
                }
            )
    }

    private fun createNotification(token: String): Notification {
        val notificationContent = NotificationContent(
            title = "vipul",
            body = binding.edtMessage.text.toString()
        )

        val additionalData = mapOf(
            "key1" to "value1",
            "key2" to "value2"
        )

        val message = Message(
            token = token,
            notification = notificationContent,
            data = additionalData
        )

        return Notification(message)
    }


    private fun findDisplayNameOfCurrentUserFromFirestore() {
        val userId = firebaseAuth.currentUser?.uid
        Log.e("idName", userId.toString())

        if (userId != null) {
            val userDocRef = db.collection("ChatUser").document(userId)
            userDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val displayName = documentSnapshot.getString("userName")
                        if (displayName != null) {
                            userName = displayName
                            Log.d("Firestore", "User name found: $displayName")
                        } else {
                            Log.e("Firestore", "Display name (userName) not found in document")
                        }
                    } else {
                        Log.e("Firestore", "User document does not exist in ChatUser collection")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error fetching document", exception)
                }
        } else {
            Log.e("Firestore", "User is not logged in")
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(messageList, firebaseAuth.currentUser!!.uid)
        recyclerView.adapter = chatAdapter
    }

    private fun loadMessagesFromFirebase() {
        val data = firebaseDatabase.getReference("ChatData")
        data.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(UserMessage::class.java)
                    if (message != null) {
                        messageList.add(message)
                        Log.d("FirebaseData", "Message added: ${message.message}")
                    } else {
                        Log.e("FirebaseData", "Message could not be parsed")
                    }
                }

                chatAdapter.notifyDataSetChanged()
                if (messageList.isNotEmpty()) {
                    recyclerView.scrollToPosition(messageList.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun init() {
        db = Firebase.firestore
        message = binding.edtMessage
        sendButton = binding.imgSendMessage
        recyclerView = binding.rwChat
    }

}