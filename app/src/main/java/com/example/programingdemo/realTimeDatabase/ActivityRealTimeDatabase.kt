package com.example.programingdemo.realTimeDatabase

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityRealTimeDatabaseBinding
import com.example.programingdemo.utlis.Const.SAVE
import com.example.programingdemo.utlis.Const.UPDATE

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
        binding.btnSave.setOnClickListener {
            val text = binding.edtNote.text.trim().toString()
            if (text.isNotEmpty()) {
                if (updateId != null) {
                    viewModel.updateUserProfile(updateId!!, text)
                    resetUpdateState()
                } else {
                    viewModel.addUserProfile(text)
                    resetUpdateState()
                }
            } else {
                Toast.makeText(
                    this@ActivityRealTimeDatabase, R.string.enter_note, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeData() {
        viewModel.userProfileInfoList.observe(this, Observer { dataList ->
            showValue(dataList)
        })
    }

    private fun showValue(dataList: List<UserProfileInfo>) {
        binding.pbMain.visibility = View.GONE
        adapter = RealtimeDatabaseAdapter(dataList, this@ActivityRealTimeDatabase)
        binding.rwMain.adapter = adapter
    }

    private fun resetUpdateState() {
        binding.btnSave.text = SAVE
        binding.edtNote.text.clear()
        updateId = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }

    override fun onDeleteClick(item: UserProfileInfo) {
        viewModel.deleteUserProfile(item.id)
    }

    override fun onUpdateClick(item: UserProfileInfo) {
        updateId = item.id
        binding.edtNote.setText(item.name.firstName)
        binding.btnSave.text = UPDATE
    }
}
