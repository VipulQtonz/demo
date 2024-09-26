package com.example.programingdemo.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programingdemo.R
import com.example.programingdemo.adapters.ContactAdapter
import com.example.programingdemo.data.Contact
import com.example.programingdemo.databinding.ActivityContactListBinding
import com.example.programingdemo.utlis.Const.CONTACT_ID
import com.example.programingdemo.utlis.Const.CONTACT_NAME
import com.example.programingdemo.utlis.Const.REQUEST_CODE_READ_CONTACTS

class ActivityContactList : AppCompatActivity() {
    private lateinit var binding: ActivityContactListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.llActivityContactListMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_READ_CONTACTS
            )
        } else {
            loadContacts()
        }
    }

    private fun loadContacts() {
        val contactsList = mutableListOf<Contact>()
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            while (it.moveToNext()) {
                val id = it.getString(idIndex) ?: continue
                val name = it.getString(nameIndex) ?: continue
                contactsList.add(Contact(id, name))
            }
        }

        if (binding.rvContact.adapter is ContactAdapter) {
            (binding.rvContact.adapter as ContactAdapter).updateContacts(contactsList)
        }
    }

    private fun setupRecyclerView() {
        binding.rvContact.layoutManager = LinearLayoutManager(this)
        binding.rvContact.adapter = ContactAdapter(emptyList()) { contact ->
            val intent = Intent(this, ActivityEditContact::class.java).apply {
                putExtra(CONTACT_ID, contact.id)
                putExtra(CONTACT_NAME, contact.name)
            }
            startActivity(intent)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}