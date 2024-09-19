package com.example.programingdemo.gallery

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityImageDetailBinding
import com.example.programingdemo.room.AppDatabase
import com.example.programingdemo.utlis.Const
import com.example.programingdemo.utlis.Const.POSITION
import com.example.programingdemo.utlis.Const.REQUEST_CODE_MANAGE_STORAGE
import com.example.programingdemo.utlis.Const.REQUEST_CODE_WRITE_STORAGE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageDetailActivity : AppCompatActivity() {
    private var imageList: MutableList<String> = mutableListOf()
    private var currentImagePosition: Int = 0
    private lateinit var binding: ActivityImageDetailBinding
    private lateinit var database: AppDatabase
    private lateinit var back: ImageView
    private var isRecentFolder: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityImageDetailMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        addOnClickListener()
    }

    private fun addOnClickListener() {
        binding.imgMenu.setOnClickListener { view ->
            showPopupMenu(view)
        }
        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun init() {
        back = binding.imgBack
        database = AppDatabase.getDatabase(this)
        imageList =
            intent.getStringArrayListExtra(Const.IMAGE_LIST)?.toMutableList() ?: mutableListOf()
        currentImagePosition = intent.getIntExtra(Const.IMAGE_POSITION, 0)
        isRecentFolder = intent.getIntExtra(POSITION, 1)

        val adapter = ImagePagerAdapter(imageList) { position ->
            currentImagePosition = position
        }
        binding.vpImageDetailsActivity.adapter = adapter
        binding.vpImageDetailsActivity.setCurrentItem(currentImagePosition, false)
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.image_option_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete -> {
                    checkPermissions()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun deleteImageFromContentProvider(position: Int) {
        val imagePath = imageList[position]
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val cursor = contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media._ID),
            "${MediaStore.Images.Media.DATA} = ?",
            arrayOf(imagePath),
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val imageUri = ContentUris.withAppendedId(uri, id)
                try {
                    CoroutineScope(Dispatchers.IO).launch {
                        val image = database.userDao().getImageByPath(imagePath)
                        if (isRecentFolder == 0) {
                            if (image != null) {
                                database.userDao().deleteRecentImage(image)
                            }
                        } else {
                            if (image != null) {
                                database.userDao().deleteRecentImage(image)
                            }
                            contentResolver.delete(imageUri, null, null)
                        }

                        withContext(Dispatchers.Main) {
                            (binding.vpImageDetailsActivity.adapter as ImagePagerAdapter).removeImage(
                                position
                            )

                            if (imageList.isNotEmpty()) {
                                currentImagePosition = position.coerceAtMost(imageList.size - 1)
                                binding.vpImageDetailsActivity.setCurrentItem(
                                    currentImagePosition, false
                                )
                            } else {
                                finish()
                            }
                        }
                    }
                } catch (e: SecurityException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this, getString(R.string.unable_to_delete_the_image), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, REQUEST_CODE_MANAGE_STORAGE)
            } else {
                deleteImageFromContentProvider(currentImagePosition)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_STORAGE
                )
            } else {
                deleteImageFromContentProvider(currentImagePosition)
            }
        } else {
            deleteImageFromContentProvider(currentImagePosition)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                deleteImageFromContentProvider(currentImagePosition)
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
