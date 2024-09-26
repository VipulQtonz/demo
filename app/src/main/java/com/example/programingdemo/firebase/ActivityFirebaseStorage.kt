package com.example.programingdemo.firebase

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.programingdemo.MyApp.Companion.firebaseStorage
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityFirebaseStorageBinding
import com.example.programingdemo.databinding.SingleImageBinding
import com.example.programingdemo.utlis.Const.CANCEL
import com.example.programingdemo.utlis.Const.DELETE
import com.example.programingdemo.utlis.Const.DOWNLOAD
import com.example.programingdemo.utlis.Const.IMAGE_FOLDER_PATH
import com.example.programingdemo.utlis.Const.JPG_EXTENSION
import com.example.programingdemo.utlis.Const.SELECTION_IMAGE

class ActivityFirebaseStorage : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFirebaseStorageBinding
    private var selectedImageUri: Uri? = null
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.imgSelectImage.setImageURI(uri)
                selectedImageUri = uri
            } else {
                binding.imgSelectImage.setImageResource(R.drawable.upload_svg)
                selectedImageUri = null
            }
        }
    private var permission = 0
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            permission = if (it) {
                1
            } else {
                0
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFirebaseStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clActivityFirebaseStorageMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        addOnClickListener()
        fetchImagesFromFirebase()
    }

    private fun setupRecyclerView() {
        binding.rwFirebaseStorage.layoutManager = GridLayoutManager(this, 3)
        binding.rwFirebaseStorage.adapter = ImageAdapterForFirebaseStorage(object :
            ImageAdapterForFirebaseStorage.OnImageClickListener {
            override fun onImageClick(imagePath: String) {
                val dialogBinding = SingleImageBinding.inflate(layoutInflater)
                Glide.with(this@ActivityFirebaseStorage).load(imagePath)
                    .into(dialogBinding.imgPreview)
                val dialog =
                    AlertDialog.Builder(this@ActivityFirebaseStorage).setView(dialogBinding.root)
                        .setPositiveButton(DELETE) { dialog, _ ->
                            deleteImageFromFirebase(imagePath)
                            dialog.dismiss()
                        }.setNeutralButton(DOWNLOAD) { dialog, _ ->
                            requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            if (permission == 1) {
                                downloadImageFromFirebase(imagePath)
                            } else {
                                Toast.makeText(
                                    this@ActivityFirebaseStorage,
                                    R.string.permission_denied,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            dialog.dismiss()
                        }.setNegativeButton(CANCEL) { dialog, _ ->
                            dialog.dismiss()
                        }.setCancelable(true).create()
                dialog.show()
            }
        })
    }

    private fun addOnClickListener() {
        binding.btnUpload.setOnClickListener(this)
        binding.imgSelectImage.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnUpload -> {
                if (selectedImageUri == null) {
                    showToast(getString(R.string.please_select_image))
                } else {
                    selectedImageUri?.let { uploadImageToFirebase(it) }
                }
            }

            R.id.imgSelectImage -> {
                imagePickerLauncher.launch(SELECTION_IMAGE)
            }
        }
    }

    private fun uploadImageToFirebase(uri: Uri) {
        val storageRef =
            firebaseStorage.reference.child("$IMAGE_FOLDER_PATH${System.currentTimeMillis()}$JPG_EXTENSION")
        val uploadTask = storageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                showToast(getString(R.string.image_uploaded_successfully))
                binding.imgSelectImage.setImageResource(R.drawable.upload_svg)
                selectedImageUri = null
                fetchImagesFromFirebase()
            }.addOnFailureListener {
                selectedImageUri = null
                showToast(getString(R.string.image_upload_failed))
            }
        }.addOnFailureListener {
            selectedImageUri = null
            showToast(getString(R.string.image_upload_failed))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun fetchImagesFromFirebase() {
        val storageRef = firebaseStorage.reference.child(IMAGE_FOLDER_PATH)
        storageRef.listAll().addOnSuccessListener { listResult ->
            val imageUrls = mutableListOf<String>()
            listResult.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    imageUrls.add(uri.toString())
                    if (imageUrls.size == listResult.items.size) {
                        updateRecyclerView(imageUrls)
                    }
                }
            }
        }
    }

    private fun updateRecyclerView(imageUrls: List<String>) {
        binding.pbFirebaseStorage.visibility = View.GONE
        val adapter = binding.rwFirebaseStorage.adapter as ImageAdapterForFirebaseStorage
        adapter.submitList(imageUrls)
    }

    private fun downloadImageFromFirebase(imagePath: String) {
        val storageRef = firebaseStorage.getReferenceFromUrl(imagePath)
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            startDownload(uri)
        }.addOnFailureListener {
            showToast(getString(R.string.failed_to_retrieve_download_url))
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun startDownload(uri: Uri) {
        val request = DownloadManager.Request(uri).setTitle(getString(R.string.downloading_image))
            .setDescription(getString(R.string.image_is_being_downloaded))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                getString(R.string.downloaded_image_jpg, System.currentTimeMillis())
            ).setAllowedOverMetered(true).setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        showToast(getString(R.string.download_started))
    }

    private fun deleteImageFromFirebase(imagePath: String) {
        firebaseStorage.getReferenceFromUrl(imagePath).delete().addOnSuccessListener {
            showToast(getString(R.string.image_deleted_successfully))
            fetchImagesFromFirebase()
        }.addOnFailureListener {
            showToast(getString(R.string.image_deleted_failed))
        }
    }
}
