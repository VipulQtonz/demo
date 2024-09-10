package com.example.programingdemo.activities

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.CONTACT_ID
import com.example.programingdemo.utlis.Const.CONTACT_NAME
import com.example.programingdemo.utlis.Const.REQUEST_CODE_WRITE_CONTACTS

class ActivityEditContact : AppCompatActivity() {

    private var contactId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        val edtContactName = findViewById<EditText>(R.id.edtContactName)
        val btnSave = findViewById<Button>(R.id.btnSave)

        contactId = intent.getStringExtra(CONTACT_ID)
        val contactName = intent.getStringExtra(CONTACT_NAME)

        edtContactName.setText(contactName)

        btnSave.setOnClickListener {
            val newName = edtContactName.text.toString().trim()
            if (newName.isNotEmpty() && contactId != null) {
                checkAndRequestPermissions(contactId!!, newName)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.contact_name_cannot_be_empty), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getContactDataId(contactId: String): String? {
        val projection = arrayOf(ContactsContract.Data._ID)
        val selection =
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?"
        val selectionArgs = arrayOf(
            contactId,
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
        )

        val cursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndex(ContactsContract.Data._ID)
                return it.getString(idIndex)
            }
        }
        return null
    }


    private fun updateContact(dataId: String, newName: String) {
        val contentValues = ContentValues().apply {
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newName)
        }

        val uri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, dataId.toLong())
        val rowsUpdated = contentResolver.update(uri, contentValues, null, null)

        if (rowsUpdated > 0) {
            Toast.makeText(this, getString(R.string.contact_updated), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.failed_to_update_contact), Toast.LENGTH_SHORT)
                .show()
        }
        finish()
    }


    private fun checkAndRequestPermissions(id: String, newName: String) {
        val writeContactsPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)

        if (writeContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_CONTACTS),
                REQUEST_CODE_WRITE_CONTACTS
            )
        } else {
            val dataId = getContactDataId(id)
            if (dataId != null) {
                updateContact(dataId, newName)
            } else {
                Toast.makeText(this, getString(R.string.contact_data_not_found), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contactId?.let { id ->
                    val newName = findViewById<EditText>(R.id.edtContactName).text.toString().trim()
                    if (newName.isNotEmpty()) {
                        updateContact(id, newName)
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
