package com.example.programingdemo.fragments.whatsapp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.CAMERAX_APP
import com.example.programingdemo.utlis.Const.REQUEST_CODE_PERMISSIONS
import com.example.programingdemo.utlis.Const.REQUIRED_PERMISSIONS
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutionException

class FragmentCamera : Fragment() {

    private lateinit var previewView: PreviewView
    private lateinit var captureButton: FloatingActionButton
    private var imageCapture: ImageCapture? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI(view)
        setupPermissionsAndCamera()
    }

    private fun initializeUI(view: View) {
        previewView = view.findViewById(R.id.preview_view)
        captureButton = view.findViewById(R.id.capture_button)
        captureButton.setOnClickListener { takePhoto() }
    }

    private fun setupPermissionsAndCamera() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                setupCameraProvider(cameraProvider)
            } catch (exc: ExecutionException) {
                showToast(R.string.camera_initialization_failed)
            } catch (exc: InterruptedException) {
                showToast(R.string.camera_initialization_interrupted)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setupCameraProvider(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        imageCapture = ImageCapture.Builder().build()
        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            this as LifecycleOwner, cameraSelector, preview, imageCapture
        )
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val outputOptions = ImageCapture.OutputFileOptions.Builder(createFile()).build()

        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { uri ->
                        val file = File(uri.path!!)
                        scanFile(file)
                        showImageDialog(file)
                        showToast(R.string.photo_capture_succeeded, uri.toString())
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.message?.let { showToast(R.string.photo_capture_failed, it) }
                }
            })
    }

    private fun createFile(): File {
        val timestamp = SimpleDateFormat(
            getString(R.string.yyyymmdd_hhmmss), Locale.US
        ).format(System.currentTimeMillis())
        val fileName = "IMG_$timestamp.jpg"
        val photoDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            CAMERAX_APP
        )

        if (!photoDir.exists()) {
            photoDir.mkdirs()
        }
        return File(photoDir, fileName)
    }

    private fun scanFile(file: File) {
        val mediaScannerIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScannerIntent.data = Uri.fromFile(file)
        requireContext().sendBroadcast(mediaScannerIntent)
    }

    private fun showImageDialog(file: File) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.single_image, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.imgPreview)
        imageView.setImageURI(Uri.fromFile(file))

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .show()
    }

    private fun showToast(messageResId: Int, vararg args: Any) {
        Toast.makeText(requireContext(), getString(messageResId, *args), Toast.LENGTH_SHORT).show()
    }
}
