package com.example.programingdemo.room

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programingdemo.R
import com.example.programingdemo.adapters.RoomAdapter
import com.example.programingdemo.databinding.ActivityRoomDatabaseBinding


class ActivityRoomDatabase : AppCompatActivity(), RoomAdapter.OnChatItemClickListener {
    private lateinit var userDao: UserDao
    private lateinit var binding: ActivityRoomDatabaseBinding
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(userDao)
    }
    private lateinit var adapter: RoomAdapter
    private var isUpdateMode: Boolean = false
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRoomDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        getAllData()
        addOnClickListener()
    }

    private fun addOnClickListener() {
        binding.btnSave.setOnClickListener {
            if (isUpdateMode) {
                updateData()
            } else {
                saveData()
            }
        }
    }

    private fun saveData() {
        if (binding.edtUserName.text.trim().isNotEmpty() && binding.edtUserEmail.text.trim()
                .isNotEmpty()
        ) {
            userViewModel.insert(
                User(
                    name = binding.edtUserName.text.trim().toString(),
                    email = binding.edtUserEmail.text.trim().toString()
                )
            )
            clearInputFields()
        } else {
            Toast.makeText(this, getString(R.string.please_fill_all_the_values), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updateData() {
        if (binding.edtUserName.text.trim().isNotEmpty() && binding.edtUserEmail.text.trim()
                .isNotEmpty()
        ) {
            userViewModel.update(
                User(
                    id = currentUserId,
                    name = binding.edtUserName.text.trim().toString(),
                    email = binding.edtUserEmail.text.trim().toString()
                )
            )
            clearInputFields()
            isUpdateMode = false
            binding.btnSave.text = getString(R.string.save)
        } else {
            Toast.makeText(this, getString(R.string.please_fill_all_the_values), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun clearInputFields() {
        binding.edtUserName.text.clear()
        binding.edtUserEmail.text.clear()
    }

    private fun getAllData() {
        userViewModel.allUsers.observe(this, Observer { users ->
            users?.let {
                adapter.getAllData(it)
            }
        })
    }

    private fun init() {
        adapter = RoomAdapter(this)
        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()
        binding.rwRoom.layoutManager = LinearLayoutManager(this)
        binding.rwRoom.adapter = adapter
    }

    override fun onUpdateClick(user: User) {
        binding.edtUserName.setText(user.name)
        binding.edtUserEmail.setText(user.email)
        binding.btnSave.text = getString(R.string.update)
        isUpdateMode = true
        currentUserId = user.id
    }

    override fun onDeleteClick(user: User) {
        userViewModel.delete(user)
    }
}
