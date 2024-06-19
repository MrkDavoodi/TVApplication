package com.example.tvapplication.commons.internet

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.registerReceiver
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit


fun downloadVideos(downloadPath: String, context: Context, fileName: String): File? {
    try {
        val direct = File(
            Environment.DIRECTORY_DOWNLOADS
                .toString() + "/Folder/"
        )
        if (!direct.exists()) {
            direct.mkdirs()
        }
        val videoFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "/Folder/$fileName"
        )
        if (!videoFile.exists() || videoFile.length().toInt() == 0) {

            val downloadUri = Uri.parse(downloadPath)
            val request = DownloadManager.Request(
                downloadUri
            )

            request.setAllowedNetworkTypes(
                (
                        DownloadManager.Request.NETWORK_WIFI
                                or DownloadManager.Request.NETWORK_MOBILE)
            )
                .setAllowedOverRoaming(false)
                .setTitle("Downloading...")
                .setDescription("Something useful. No, really.")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "/Folder/$fileName"
                )
            Log.d("path Download", Environment.DIRECTORY_DOWNLOADS.toString())
            val downloadId =
                (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(
                    request
                )



            return if (isFileDownloaded(context, downloadId)) videoFile else null
        }

        return null
    } catch (e: Exception) {
        Log.d("Exception", e.message.toString())
        return null

    }
}

fun isFileDownloaded(context: Context, downloadId: Long): Boolean {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val query = DownloadManager.Query()
    query.setFilterById(downloadId)

    val cursor = downloadManager.query(query)
    if (cursor.moveToFirst()) {
        val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
        return status == DownloadManager.STATUS_SUCCESSFUL
    }

    return false
}
fun checkStatus(context: Context){
    val onComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                // Handle download completion here
            }
        }
    }

    context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
}

fun downloadFile(url: String, context: Context, fileName: String): File?  {
    try {


    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val request = DownloadManager.Request(Uri.parse(url))

    request.setAllowedNetworkTypes(
        (
                DownloadManager.Request.NETWORK_WIFI
                        or DownloadManager.Request.NETWORK_MOBILE)
    )
        .setTitle("Downloading Video")
        .setDescription("Downloading $fileName")
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Folder/$fileName")

    downloadManager.enqueue(request)

    val query = DownloadManager.Query()
    query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL)

    val cursor = downloadManager.query(query)

    if (cursor.moveToFirst()) {
        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
        val status = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val isSuccess=status == DownloadManager.STATUS_SUCCESSFUL

        val filePath = cursor.getString(columnIndex)

        val direct = File(
            Environment.DIRECTORY_DOWNLOADS
                .toString() + "/Folder/"
        )
        if (!direct.exists()) {
            direct.mkdirs()
        }
    }
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "/Folder/$fileName")
    }catch (e:Exception){
        Log.d("Exception", e.message.toString())
        return  null
    }
}



