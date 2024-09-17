package com.example.programingdemo.contentProvider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.AUTHORITY
import com.example.programingdemo.utlis.Const.COLUMN_ID
import com.example.programingdemo.utlis.Const.MY_TABLE
import com.example.programingdemo.utlis.Const.MY_TABLE_ID
import com.example.programingdemo.utlis.Const.TABLE_NAME
import com.example.programingdemo.utlis.Const.TABLE_NAME_HASH
import com.example.programingdemo.utlis.MyDatabaseHelper

class ContentProviderMy : ContentProvider() {
    private lateinit var dbHelper: SQLiteOpenHelper
    private lateinit var uriMatcher: UriMatcher

    override fun onCreate(): Boolean {
        dbHelper = MyDatabaseHelper(context!!)
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, MY_TABLE)
            addURI(AUTHORITY, TABLE_NAME_HASH, MY_TABLE_ID)
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        val match = uriMatcher.match(uri)
        return when (match) {
            MY_TABLE -> db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )


            MY_TABLE_ID -> {
                val id = uri.lastPathSegment
                val newSelection = "$COLUMN_ID = ?"
                val newSelectionArgs = arrayOf(id)
                db.query(
                    TABLE_NAME,
                    projection,
                    newSelection,
                    newSelectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }

            else -> throw IllegalArgumentException(context!!.getString(R.string.unknown_uri, uri))
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val db = dbHelper.writableDatabase
        val id = db.insert(TABLE_NAME, null, values)
        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, id)
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?
    ): Int {
        val db = dbHelper.writableDatabase
        val count = if (values != null) {
            db.update(
                TABLE_NAME, values, selection, selectionArgs
            )
        } else {
            0
        }

        context?.contentResolver?.notifyChange(uri, null)
        return count
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        val count = db.delete(TABLE_NAME, selection, selectionArgs)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String {
        return context!!.getString(R.string.vnd_android_cursor_dir_vnd_mytable, AUTHORITY)
    }
}