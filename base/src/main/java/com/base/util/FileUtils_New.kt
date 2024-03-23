package com.base.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.webkit.WebViewFragment

import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.WorkerThread
import androidx.core.net.toUri
import com.base.R
import java.io.*


object FileUtils_New {
    private const val TAG = "FileUtils_New"

    fun sendGalleryForPDFIntent(launcher: ActivityResultLauncher<String>) {
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

        launcher.launch("application/pdf")
        //fragment.startActivityForResult(intent, requestCode)
    }
    @WorkerThread
    fun getReadablePathFromUri(context: Context, uri: Uri): String? {
        try {
        var path: String? = null
        if ("file".equals(uri.scheme, ignoreCase = true)) {
            path = uri.path
        }
        else if ("content".equals(uri.scheme, ignoreCase = true)) {
            path = getDataColumn(context, uri, null, null)
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            path = getPath(context, uri)
        }
        if (TextUtils.isEmpty(path)) {
            return path
        }
        Log.d(TAG, "get path from uri: $path")
        if (!FileUtils_New.isReadablePath(path)) {
            val index = path!!.lastIndexOf("/")
            val name = path.substring(index + 1)
            val dstPath = context.cacheDir.absolutePath + File.separator + name
            if (copyFile(context, uri, dstPath)) {
                path = dstPath
                Log.d(TAG, "copy file success: $path")
            } else {
                Log.d(TAG, "copy file fail!")
            }
        }
        return path
        }catch (e: Exception) {
                return e.toString()
        }

    }

//    fun getPath(context: Context?, uri: Uri): String? {
//        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            if (FileUtils_New.isExternalStorageDocument(uri)) {
//                val docId = DocumentsContract.getDocumentId(uri)
//                Log.d("External Storage", docId)
//                val split = docId.split(":").toTypedArray()
//                val type = split[0]
//                if ("primary".equals(type, ignoreCase = true)) {
//                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
//                }
//            } else if (isDownloadsDocument(uri)) {
//                val dstPath =
//                    context?.cacheDir?.absolutePath + File.separator + FileUtils_New.getFileName(
//                        context,
//                        uri
//                    )
//                if (FileUtils_New.copyFile(context, uri, dstPath)) {
//                    Log.d(FileUtils_New.TAG, "copy file success: $dstPath")
//                    return dstPath
//                } else {
//                    Log.d(FileUtils_New.TAG, "copy file fail!")
//                }
//            } else if (FileUtils_New.isMediaDocument(uri)) {
//                val docId = DocumentsContract.getDocumentId(uri)
//                val split = docId.split(":").toTypedArray()
//                val type = split[0]
//                var contentUri: Uri? = null
//                if ("image" == type) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                } else if ("video" == type) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//                } else if ("audio" == type) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//                }
//                else{
//                    val dstPath =
//                        context?.cacheDir?.absolutePath + File.separator + FileUtils_New.getFileName(
//                            context,
//                            uri
//                        )
//                    if (FileUtils_New.copyFile(context, uri, dstPath)) {
//                        Log.d(FileUtils_New.TAG, "copy file success: $dstPath")
//                        return dstPath
//                    } else {
//                        Log.d(FileUtils_New.TAG, "copy file fail!")
//                    }
//                }
//                val selection = "_id=?"
//                val selectionArgs = arrayOf(split[1])
//                return getDataColumn(context, contentUri!!, selection, selectionArgs)
//            }
//        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
//            return FileUtils_New.getDataColumn(context, uri, null, null)
//        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
//            return uri.path
//        }
//        return null
//    }

    @Throws(IOException::class)
    fun getPath(context: Context, uri: Uri): String? {
        val destinationFilename =
            File(context.filesDir.path + File.separatorChar + queryName(context, uri))
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                ins?.let {
                    createFileFromStream(
                        it,
                        destinationFilename
                    )
                }
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message.toString())
            ex.printStackTrace()
        }
        return destinationFilename.path
    }

    fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message.toString())
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    fun getFileName(context: Context?, uri: Uri?): String {
        val cursor = context?.contentResolver?.query(uri!!, null, null, null, null)
        val nameindex = cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        return cursor.getString(nameindex)
    }

    private fun getDataColumn(
        context: Context?, uri: Uri, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context?.contentResolver?.query(
                uri, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isReadablePath(path: String?): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }
        val isLocalPath: Boolean
        isLocalPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!TextUtils.isEmpty(path)) {
                val localFile = File(path)
                localFile.exists() && localFile.canRead()
            } else {
                false
            }
        } else {
            path!!.startsWith(File.separator)
        }
        return isLocalPath
    }

    private fun copyFile(context: Context?, uri: Uri, dstPath: String): Boolean {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            inputStream = context?.contentResolver?.openInputStream(uri)
            outputStream = FileOutputStream(dstPath)
            val buff = ByteArray(100 * 1024)
            var len: Int
            while (inputStream!!.read(buff).also { len = it } != -1) {
                outputStream.write(buff, 0, len)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return true
    }

    fun getFileSize(file: File): Double {
        // Get length of file in bytes
        val fileSizeInBytes = file.length()
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        val fileSizeInKB = fileSizeInBytes / 1024
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)

        return (fileSizeInKB / 1024.0).toDouble()
    }

    private fun getMediaDocumentPath(context: Context?, uri: Uri?,selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            if (uri == null) return null
            cursor = context?.contentResolver?.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                //Toast.makeText(context, "From Media Document --- Non Media Path ${cursor.getString(index)} ", Toast.LENGTH_SHORT).show()
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }
    fun openFile(context: Context,uri: Uri, onError : (Exception) -> Unit) {
        try {
            // val uri = Uri.fromFile(uri)
            val intent = Intent(Intent.ACTION_VIEW)
            if (uri.toString().contains(".doc") || uri.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword")
            } else if (uri.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf")
            } else if (uri.toString().contains(".ppt") || uri.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
            } else if (uri.toString().contains(".xls") || uri.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel")
            } else if (uri.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip")
            } else if (uri.toString().contains(".rar")) {
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed")
            } else if (uri.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf")
            } else if (uri.toString().contains(".wav") || uri.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav")
            } else if (uri.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif")
            } else if (uri.toString().contains(".jpg") || uri.toString()
                    .contains(".jpeg") || uri.toString().contains(".png")
            ) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg")
            } else if (uri.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain")
            } else if (uri.toString().contains(".3gp") || uri.toString().contains(".mpg") ||
                uri.toString().contains(".mpeg") || uri.toString()
                    .contains(".mpe") || uri.toString().contains(".mp4") || uri.toString()
                    .contains(".avi")
            ) {
                // Video files
                intent.setDataAndType(uri, "video/*")
            } else {
                intent.setDataAndType(uri, "*/*")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            try {
                if (uri.toString().contains(".pdf")) {
                    var newuri = "ttp://docs.google.com/viewer?url=$uri"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(newuri))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    onError(e)
                }
            }catch (e:Exception) {
                e.printStackTrace()
            }

//            Toast.makeText(
//                context,
//                "No application found which can open the file",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

}