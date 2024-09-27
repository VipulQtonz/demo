package com.example.programingdemo.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programingdemo.R
import com.example.programingdemo.adapters.ChatUserAdapter
import com.example.programingdemo.data.UserData
import com.example.programingdemo.databinding.ActivityChatUserBinding
import com.example.programingdemo.utlis.Const.USER_DATA_CHAT
import com.example.programingdemo.viewModel.ChatUserViewModel

class ActivityChatUser : AppCompatActivity(), ChatUserAdapter.OnUserClickListener {
    private lateinit var binding: ActivityChatUserBinding
    private lateinit var chatUserAdapter: ChatUserAdapter
    private val viewModel: ChatUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clActivityChatUSerMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initRecyclerView()
        observeUsers()
    }

    private fun initRecyclerView() {
        chatUserAdapter = ChatUserAdapter(ArrayList(), this)
        binding.reAllChatUser.layoutManager = LinearLayoutManager(this)
        binding.reAllChatUser.adapter = chatUserAdapter
    }

    private fun observeUsers() {
        viewModel.userListLiveData.observe(this) { userList ->
            chatUserAdapter.updateUsers(userList)
        }
        viewModel.fetchAllUsers()
    }

    override fun onUserClick(user: UserData) {
        val intent = Intent(this, ActivityChat::class.java)
        intent.putExtra(USER_DATA_CHAT, user)
        startActivity(intent)
    }
}
