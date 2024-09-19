package com.example.programingdemo.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.RECENT
import com.example.programingdemo.utlis.Const.REQUEST_CODE_READ_EXTERNAL_STORAGE
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class ActivityDeviceImage : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var folderPagerAdapter: FolderPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_device_image)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clActivityDeviceImageMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {
        viewPager = findViewById(R.id.vpMain)
        tabLayout = findViewById(R.id.tbMain)
        if (hasReadStoragePermission()) {
            setupViewPager()
        } else {
            requestReadStoragePermission()
        }

    }

    private fun requestReadStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_CODE_READ_EXTERNAL_STORAGE
            )

        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun hasReadStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun setupViewPager() {
        CoroutineScope(Dispatchers.IO).launch {
            val folderList = getImageFolders()
            folderList.add(0, Pair(RECENT, RECENT.lowercase(Locale.ROOT)))

            withContext(Dispatchers.Main) {
                folderPagerAdapter =
                    FolderPagerAdapter(this@ActivityDeviceImage, folderList)
                viewPager.adapter = folderPagerAdapter
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = folderList[position].first
                }.attach()
                viewPager.setPageTransformer(ZoomOutPageTransformer())
                viewPager.setCurrentItem(1, false)
            }
        }
    }

    private fun getImageFolders(): MutableList<Pair<String, String>> {
        val folderList = mutableListOf<Pair<String, String>>()
        val projection =
            arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null
        )

        cursor?.use {
            val columnIndexFolderName =
                it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            val folderPaths = mutableSetOf<String>()

            while (it.moveToNext()) {
                val folderName = it.getString(columnIndexFolderName)
                val folderPath = it.getString(columnIndexData).substringBeforeLast("/")

                if (!folderPaths.contains(folderPath)) {
                    folderPaths.add(folderPath)
                    folderList.add(Pair(folderName, folderPath))
                }
            }
        }
        return folderList
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupViewPager()
                } else {
                    Toast.makeText(
                        this, getString(R.string.please_give_permission), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
