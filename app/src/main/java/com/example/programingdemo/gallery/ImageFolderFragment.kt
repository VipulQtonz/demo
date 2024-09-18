package com.example.programingdemo.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programingdemo.R
import com.example.programingdemo.adapters.ImageAdapter
import com.example.programingdemo.room.AppDatabase
import com.example.programingdemo.utlis.Const.FOLDER_PATH
import com.example.programingdemo.utlis.Const.IMAGE_LIST
import com.example.programingdemo.utlis.Const.IMAGE_POSITION
import com.example.programingdemo.utlis.Const.POSITION
import com.example.programingdemo.utlis.Const.RECENT
import com.example.programingdemo.utlis.Const.REQUEST_CODE_READ_EXTERNAL_STORAGE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class ImageFolderFragment : Fragment(), ImageAdapter.OnImageClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_folder, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getFolderPath()
    }

    private fun getFolderPath() {
        val folderPath = arguments?.getString(ARG_FOLDER_PATH)
        folderPath?.let {
            checkPermissions(it)
        }
    }

    private fun init(view: View) {
        recyclerView = view.findViewById(R.id.rvImage)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        adapter = ImageAdapter(this)
        recyclerView.adapter = adapter
        database = AppDatabase.getDatabase(requireContext())
    }

    private fun loadImagesFromFolder(folderPath: String) {
        if (folderPath == RECENT.lowercase(Locale.ROOT)) {
            database.userDao().getAllRecentPhotos().observe(viewLifecycleOwner) { recentImages ->
                val imagePaths = recentImages.map { it.path }
                adapter.submitList(imagePaths)
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val projection = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA
                )

                val selection = "${MediaStore.Images.Media.DATA} LIKE ?"
                val selectionArgs = arrayOf("$folderPath/%")

                val cursor = requireContext().contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    "${MediaStore.Images.Media.DATE_ADDED} DESC"
                )

                cursor?.use {
                    val columnIndexData = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    val newImageList = mutableListOf<String>()

                    while (it.moveToNext()) {
                        val imagePath = it.getString(columnIndexData)
                        newImageList.add(imagePath)
                    }

                    withContext(Dispatchers.Main) {
                        adapter.submitList(newImageList)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val folderPath = arguments?.getString(ARG_FOLDER_PATH)
        folderPath?.let {
            loadImagesFromFolder(it)
        }
    }

    companion object {
        private const val ARG_FOLDER_PATH = FOLDER_PATH
        fun newInstance(folderPath: String, isRecentTab: Int): ImageFolderFragment {
            val fragment = ImageFolderFragment()
            val args = Bundle()
            args.putString(ARG_FOLDER_PATH, folderPath)
            args.putInt(POSITION, isRecentTab)
            fragment.arguments = args
            return fragment
        }
    }

    private fun checkPermissions(folderPath: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_CODE_READ_EXTERNAL_STORAGE
                )
            } else {
                loadImagesFromFolder(folderPath)
            }
        } else {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_READ_EXTERNAL_STORAGE
                )
            } else {
                loadImagesFromFolder(folderPath)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val folderPath = arguments?.getString(ARG_FOLDER_PATH)
                folderPath?.let {
                    loadImagesFromFolder(it)
                }
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.permission_required_to_access_images), Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    override fun onImageClick(imagePath: String) {
        insetImageToDatabase(imagePath)
        val imageList = adapter.currentList
        val imagePosition = imageList.indexOf(imagePath)
        val position = arguments?.getInt(POSITION)
        toImageDetailsActivity(position, imageList, imagePosition)
    }

    private fun insetImageToDatabase(imagePath: String) {
        lifecycleScope.launch {
            val existingImage = database.userDao().getImageByPath(imagePath)
            if (existingImage != null) {
                database.userDao().deleteRecentImage(existingImage)
            }
            database.userDao().insertRecentImage(ImagePath(path = imagePath))
        }
    }

    private fun toImageDetailsActivity(
        position: Int?,
        imageList: List<String>,
        imagePosition: Int
    ) {
        val intent = Intent(requireContext(), ImageDetailActivity::class.java)
        intent.putExtra(POSITION, position)
        intent.putStringArrayListExtra(IMAGE_LIST, ArrayList(imageList))
        intent.putExtra(IMAGE_POSITION, imagePosition)
        startActivity(intent)
    }
}