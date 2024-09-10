package com.example.programingdemo.activities

import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programingdemo.R
import com.example.programingdemo.adapters.MyDataRecyclerView
import com.example.programingdemo.data.MyData
import com.example.programingdemo.databinding.ActivityContentProviderBinding
import com.example.programingdemo.utlis.Const.SAVE
import com.example.programingdemo.utlis.Const.UPDATE
import com.example.programingdemo.utlis.GeneralUsage

class ActivityContentProvider : AppCompatActivity(), MyDataRecyclerView.OnItemClickListener {

    private lateinit var binding: ActivityContentProviderBinding
    private lateinit var adapter: MyDataRecyclerView
    private var updateId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContentProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ActivityContentProviderMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addOnClickListener()
        getValue()
    }

    private fun addOnClickListener() {
        binding.btnSave.setOnClickListener {
            val text = binding.edtNote.text.trim().toString()
            if (text.isNotEmpty()) {
                if (updateId != null) {
                    updateValue(updateId!!, text)
                } else {
                    addValue(text)
                }
            } else {
                Toast.makeText(
                    this@ActivityContentProvider, R.string.enter_note, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun deleteValue(id: String) {

        val selection = "${GeneralUsage.MyContract.MyTable._ID} = ?"
        val selectionArgs = arrayOf(id)

        contentResolver.delete(
            GeneralUsage.MyContract.CONTENT_URI, selection, selectionArgs
        )
        getValue()

    }

    private fun updateValue(id: String, name: String) {
        val values = ContentValues().apply {
            put(
                GeneralUsage.MyContract.MyTable.COLUMN_NAME, name
            )
        }
        val selection = "${GeneralUsage.MyContract.MyTable._ID} = ?"
        val selectionArgs = arrayOf(id)

        contentResolver.update(
            GeneralUsage.MyContract.CONTENT_URI, values, selection, selectionArgs
        )
        resetUpdateState()
        getValue()

    }

    private fun getValue() {
        binding.btnSave.text = SAVE
        updateId = null
        val dataList = mutableListOf<MyData>()
        val cursor = contentResolver.query(
            GeneralUsage.MyContract.CONTENT_URI,
            arrayOf(
                GeneralUsage.MyContract.MyTable._ID,
                GeneralUsage.MyContract.MyTable.COLUMN_NAME
            ),
            null,
            null,
            "${GeneralUsage.MyContract.MyTable._ID} DESC"
        )
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(GeneralUsage.MyContract.MyTable._ID))

                val name =
                    it.getString(it.getColumnIndexOrThrow(GeneralUsage.MyContract.MyTable.COLUMN_NAME))
                dataList.add(MyData(id, name))
            }
        }
        binding.edtNote.text.clear()
        showValue(dataList)
    }

    private fun showValue(dataList: MutableList<MyData>) {
        binding.rwMain.layoutManager = LinearLayoutManager(this@ActivityContentProvider)
        adapter = MyDataRecyclerView(dataList, this@ActivityContentProvider)
        binding.rwMain.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun addValue(name: String) {
        val values = ContentValues().apply {
            put(GeneralUsage.MyContract.MyTable.COLUMN_NAME, name)
        }

        val uri = contentResolver.insert(GeneralUsage.MyContract.CONTENT_URI, values)
        uri?.let {
            getValue()
        }
    }

    private fun resetUpdateState() {
        binding.btnSave.text = SAVE
        updateId = null
    }

    override fun onDeleteClick(item: MyData) {
        deleteValue(item.id.toString())
    }

    override fun onUpdateClick(item: MyData) {
        updateId = item.id.toString()
        binding.edtNote.setText(item.name)
        binding.btnSave.text = UPDATE
    }

    override fun onResume() {
        super.onResume()
        getValue()
    }
}
