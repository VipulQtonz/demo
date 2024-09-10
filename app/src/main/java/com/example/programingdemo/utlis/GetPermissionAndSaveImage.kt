package com.example.programingdemo.utlis

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.DATA
import com.example.programingdemo.utlis.Const.EXAMPLE_IMAGE_NAME
import com.example.programingdemo.utlis.Const.MYA_PP_IMAGES
import com.example.programingdemo.utlis.Const.REQUEST_CODE_WRITE_EXTERNAL_STORAGE
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

class GetPermissionAndSaveImage(val context: Context) {
    fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE
            )
        } else {
            saveDrawableToImageFile()
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted; proceed with the image saving
//                saveDrawableToImageFile()
//            } else {
//                // Permission denied; handle appropriately
//                Toast.makeText(
//                    this,
//                    getString(R.string.permission_denied_to_write_to_external_storage),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

    private fun saveDrawableToImageFile() {
        val drawable = ContextCompat.getDrawable(context, R.drawable.user_profile) ?: return
        val bitmap = drawableToBitmap(drawable)

        val file = createImageFile()
        try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                Toast.makeText(context, R.string.image_saved_successfully, Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, R.string.failed_to_save_image, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun createImageFile(): File {
        val timestamp =
            SimpleDateFormat(
                context.getString(R.string.yyyymmdd_hhmmss),
                Locale.US
            ).format(System.currentTimeMillis())
        val fileName = "IMG_$timestamp.png"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            MYA_PP_IMAGES
        )

        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File(storageDir, fileName)
    }

    fun saveImageToFile(imageBitmap: Bitmap) {
        val picturesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val myAppDir = File(picturesDir, MYA_PP_IMAGES)

        if (!myAppDir.exists()) {
            myAppDir.mkdirs()
        }

        val file = File(myAppDir, EXAMPLE_IMAGE_NAME)
        FileOutputStream(file).use { outputStream ->
            if (imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                Log.d(
                    DATA,
                    context.getString(R.string.image_saved_successfully_in_pictures_myappimages)
                )
            } else {
                Log.e(DATA, context.getString(R.string.failed_to_save_image))
            }
        }
    }

    private fun performStorageOperation() {
        val picturesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        val myAppDir = File(picturesDir, MYA_PP_IMAGES)
        if (!myAppDir.exists()) {
            myAppDir.mkdirs()
        }

        val file = File(myAppDir, EXAMPLE_IMAGE_NAME)
        file.writeText(context.getString(R.string.hello_shared_external_storage_in_myappimages))
    }
}