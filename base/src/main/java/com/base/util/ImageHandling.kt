package com.base.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import android.R
import android.media.MediaMetadata
import android.widget.ImageView
import androidx.core.net.toUri


@Singleton
class ImageHandling @Inject constructor() {
    // open gallery for image
    fun sendGalleryForImageIntent(launcher: ActivityResultLauncher<String>, requestCode: Int) {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//
////        intent.action = android.FAQContent.Intent.ACTION_VIEW
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            intent.type = "*/*"
//            val mimetypes = arrayOf("image/*")
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
//        } else {
//            //|video/mp4
//            intent.type = "image/*"
//        }

        launcher.launch("image/*")
        //fragment.startActivityForResult(intent, requestCode)
    }

    //capture image
    fun capture(
        launcher: ActivityResultLauncher<Intent>,
        context: Context,
        applicationID: String
    ): String? {
        var uploadImagePath: String? = null
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(context.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    val file = createImageFile(context)
                    uploadImagePath = file.absolutePath
                    file
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context,
                        "$applicationID.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    launcher.launch(takePictureIntent)
                }
            }
        }
        return uploadImagePath
    }


    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    /*
   * rotate image if requried
   * for samsung devies\
   * */
    @Throws(IOException::class)
    fun rotateImageIfRequired(img: Bitmap, pathName: String): Bitmap {

        val ei = ExifInterface(pathName)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> return rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> return rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> return rotateImage(img, 270)
            else -> return img
        }
    }


    private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        var img = img
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        //        Bitmap scaledBitmap = Bitmap.createScaledBitmap(img, img.getWidth(), img.getHeight(), true);
        img = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        //        img.recycle();
        return img
    }

    fun decodeBitmapFromFile(path: String, reqWidth: Int, reqHeight: Int): Bitmap? {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)


        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeFile(path, options)
        try {
            return rotateImageIfRequired(bitmap, path)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return null
        }

    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (height / inSampleSize >= reqHeight && width / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    @Throws(Exception::class)
    fun imageCompression(inputFilePath: String, outoutFilePath: String): Boolean {
        val uploadFile = File(inputFilePath)
        val out = File(outoutFilePath)
        if (uploadFile.length() < 1000000) { //ignore if file length is almost 1 mb
            return false
        }

        val bytes: OutputStream = FileOutputStream(out)
        var bitmap = BitmapFactory.decodeFile(uploadFile.absolutePath)
        if (uploadFile.length() < 1500000) {
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
        } else {
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 4, bitmap.height / 4, true)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
        }

        return true
    }

    fun getWidthHeight(context: Context,uri: String?) :BitmapFactory.Options{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(
            context.contentResolver.openInputStream(File(uri).toUri()),
            null,
            options
        )
        return options
    }

}