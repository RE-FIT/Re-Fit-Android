package com.example.refit.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.icu.text.SimpleDateFormat
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat.requireContext
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.Locale
import java.util.UUID

object FileUtil {

    private const val MAX_WIDTH = 1280
    private const val MAX_HEIGHT = 960

    private fun getFileName(context: Context, uri: Uri): String {
        val name = uri.toString().split("/").last()
        val ext = context.contentResolver.getType(uri)!!.split("/").last()
        Timber.d("이미지 경로 : ${ext.isNullOrEmpty()}")

        return "$name.$ext"
    }

    private fun createTempFile(context: Context, fileName: String): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, fileName)
    }

    private fun copyToFile(context: Context, uri: Uri, file: File) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        val buffer = ByteArray(2 * 1024)
        while (true) {
            val byteCount = inputStream!!.read(buffer)
            if (byteCount < 0) break
            outputStream.write(buffer, 0, byteCount)
        }
        outputStream.flush()
        inputStream.close()
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    fun convertResizeImage(context: Context, uri: Uri): String? {
        try {
            var inputStream = context.contentResolver.openInputStream(uri)!!
            val exif = ExifInterface(inputStream)
            val orientation =
                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            val rotation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

                inputStream = context.contentResolver.openInputStream(uri)!!
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                val matrix = Matrix()
                matrix.setRotate(rotation.toFloat(), bitmap.width.toFloat(), bitmap.height.toFloat())

                val newImg =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                val cacheFile = File(
                    context.applicationContext.cacheDir,
                    "newImage.jpg"
                )

                val outputStream = FileOutputStream(cacheFile)
                newImg.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                outputStream.close()

                return cacheFile.absolutePath

        } catch (e:Exception) {
            Timber.d("FileUtil - ${e.message}")
        }
        return null
    }


    private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        rotatedBitmap.recycle()
        return rotatedBitmap
    }

    fun toFile(context: Context, uri: Uri): File {
        val fileName = getFileName(context, uri)

        val file = createTempFile(context, fileName)
        copyToFile(context, uri, file)

        return File(file.absolutePath)
    }

    fun createImageFile(context: Context): Uri? {
        val now = SimpleDateFormat.getDateInstance().format(Date())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "refit_${now}jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
    }
}