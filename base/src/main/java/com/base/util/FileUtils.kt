package com.base.util

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File


object FileUtils {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun getPath(context: Context, uri: Uri): String? {

        val isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                else{
                    //non-media files i.e documents and other files
                    contentUri = MediaStore.Files.getContentUri("external")
                    val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        MediaStore.MediaColumns.RELATIVE_PATH + "=?"
                    } else "_id=?"
                    val selectionArgs = arrayOf(Environment.DIRECTORY_DOCUMENTS)
                    return getMediaDocumentPath(
                        context,
                        contentUri,
                        selection,
                        selectionArgs
                    )

                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(
                    context,
                    contentUri,
                    selection,
                    selectionArgs
                )
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } catch (se: SecurityException) {
            se.printStackTrace()
        } finally {
            cursor?.close()
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun getExtension(path: String): String {
        var extension = ""
        val token = path.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        extension = token[token.size - 1]


        return extension
    }

    fun getFileName(path: String): String {
        var filename = ""
        val token = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        filename = token[token.size - 1]
        return filename
    }

    fun getFileDuration(activity: Context, path: String): Int {
        val uri = Uri.parse(path)
        val mmr = MediaMetadataRetriever()
        try {
            mmr.setDataSource(activity, uri)
        } catch (e: Exception) {
            return 0
        }

        val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

        return Integer.parseInt(durationStr)
    }

    /*
    * return the file size in mp
    * */
    fun getFileSize(file: File): Long {
        // Get length of file in bytes
        val fileSizeInBytes = file.length()
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        val fileSizeInKB = fileSizeInBytes / 1024
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)

        return fileSizeInKB / 1024
    }

    fun dpToPX(dp: Int): Int {
        return (dp * Resources.getSystem().getDisplayMetrics().density).toInt()
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes: ByteArrayOutputStream = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Image_${System.currentTimeMillis()}",
            null
        )
        return Uri.parse(path)
    }

     fun openFile(context: Context,uri: Uri) {
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
            Toast.makeText(
                context,
                "No application found which can open the file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getMediaDocumentPath(context: Context?, uri: Uri?,selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            if (uri == null) return null
            cursor = context?.contentResolver?.query(uri, null, selection, selectionArgs, null)
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

}
