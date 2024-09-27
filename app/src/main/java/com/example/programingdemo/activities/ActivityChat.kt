@file:Suppress("DEPRECATION")

package com.example.programingdemo.activities

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
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
import com.example.programingdemo.data.UserData
import com.example.programingdemo.data.UserMessage
import com.example.programingdemo.databinding.ActivityChatBinding
import com.example.programingdemo.utlis.Const.CHAT_DATA
import com.example.programingdemo.utlis.Const.CHAT_USER
import com.example.programingdemo.utlis.Const.USER_DATA_CHAT
import com.example.programingdemo.utlis.Const.USER_NAME
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityChat : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var message: EditText
    private lateinit var sendButton: ImageView
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private var receiverToken: String? = null
    private var receiverUserId: String? = null
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
        getChatData()
        setupRecyclerView()
        findDisplayNameOfCurrentUserFromFirestore()
        loadMessagesFromFirebase()
        requestPermissions()
        buttonClickEvent()

    }

    private fun buttonClickEvent() {
        sendButton.setOnClickListener {
            sendNotification()
            if (message.text.trim().toString().isNotEmpty()) {
                val userMessage = receiverUserId?.let { it1 ->
                    UserMessage(
                        userId = it1,
                        userName = userName.toString(),
                        message = message.text.trim().toString(),
                        isCurrentUser = true
                    )
                }
                val data = receiverUserId?.let { it1 ->
                    firebaseDatabase.getReference(CHAT_DATA).child(firebaseAuth.currentUser!!.uid)
                        .child(
                            it1
                        )
                }
                data!!.push().setValue(userMessage).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        message.text.clear()
                        saveDataInBackground(userMessage)
                    }
                }
            }
        }
    }

    private fun saveDataInBackground(userMessage: UserMessage?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (userMessage != null) {
                val data = UserMessage(
                    userId = userMessage.userId,
                    userName = userMessage.userName,
                    message = userMessage.message,
                    isCurrentUser = false
                )

                val chatReference = receiverUserId?.let {
                    firebaseDatabase.getReference(CHAT_DATA).child(it)
                        .child(firebaseAuth.currentUser!!.uid)
                }
                chatReference?.push()?.setValue(data)
            }
        }
    }

    private fun getChatData() {
        val userData = intent.getSerializableExtra(USER_DATA_CHAT) as? UserData
        userData?.let {
            receiverToken = it.token
            receiverUserId = it.userId
            binding.tvUserName.text = it.userName
            binding.tvEmailId.text = it.email

        }
    }

    private fun sendNotification() {
        CoroutineScope(Dispatchers.IO).launch {
            val notification = receiverToken?.let { createNotification(it) }
            if (notification != null) {
                try {
                    val accessToken = AccessToken.getAccessTokenAsync()
                    if (accessToken != null) {
                        val response = NotificationAPI.sendNotification()
                            .notification(notification, "Bearer $accessToken")
                            .execute()

                        withContext(Dispatchers.Main) {
                            if (!response.isSuccessful) {
                                val errorMsg =
                                    response.errorBody()?.string()
                                        ?: getString(R.string.unknown_error)
                                Toast.makeText(
                                    this@ActivityChat,
                                    getString(R.string.failed_to_send_notification, errorMsg),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ActivityChat, "${e.message}", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.POST_NOTIFICATIONS)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?, token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }).check()
        }
    }


    private fun createNotification(token: String): Notification? {
        val notificationContent = userName?.let {
            NotificationContent(
                title = it, body = binding.edtMessage.text.toString()
            )
        }
        val additionalData = mapOf(
            "key1" to "value1",
            "key2" to "value2"
        )

        val message = notificationContent?.let {
            Message(
                token = token, notification = it, data = additionalData
            )
        }
        return message?.let { Notification(it) }
    }

    private fun findDisplayNameOfCurrentUserFromFirestore() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            val userDocRef = db.collection(CHAT_USER).document(userId)
            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val displayName = documentSnapshot.getString(USER_NAME)
                    if (displayName != null) {
                        userName = displayName
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(messageList)
        recyclerView.adapter = chatAdapter
    }

    private fun loadMessagesFromFirebase() {
        val data = receiverUserId?.let {
            firebaseDatabase.getReference(CHAT_DATA).child(firebaseAuth.currentUser!!.uid).child(
                it
            )
        }
        data!!.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(UserMessage::class.java)
                    if (message != null) {
                        messageList.add(message)
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