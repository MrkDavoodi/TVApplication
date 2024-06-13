package com.example.tvapplication.commons.internet

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
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
        if (!videoFile.exists() || videoFile.length().toInt() ==0) {

            val downloadUri = Uri.parse(downloadPath)
            val request = DownloadManager.Request(
                downloadUri
            )

            request.setAllowedNetworkTypes(
                (
                        DownloadManager.Request.NETWORK_WIFI
                                or DownloadManager.Request.NETWORK_MOBILE)
            )
                .setAllowedOverRoaming(false).setTitle("Downloading...")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Folder/$fileName")
            Log.d("path Download", Environment.DIRECTORY_DOWNLOADS.toString())
            (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
        }
        return videoFile

    }catch (e:Exception){
        Log.d("Exception", e.message.toString())
        return  null

    }
}



@OptIn(DelicateCoroutinesApi::class)
suspend fun saveFileData(context: Context, url: String, fileName: String) {
    GlobalScope.launch {
        var videoFilePath = ""
//    val okHttpClient = OkHttpClient()

        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(context.cacheDir, cacheSize.toLong())
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()
        Log.d("downloadFile request", request.toString())

        try {
            val response: Response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                Log.d("downloadFile response", response.toString())
                val inputStream = response.body()?.byteStream()

                val path = File(context.filesDir.toString() + "/Folder")
                if (!path.exists())
                    path.mkdirs()

                val videoFile = File(path, fileName)
                if (!videoFile.exists()) {

                    videoFilePath = videoFile.toURI().toString()
                    Log.d("path", videoFilePath)

                    val fos = FileOutputStream(videoFile)
                    fos.write(inputStream?.readBytes())
                    fos.flush()
                    fos.close()
                }

            } else {
                Toast.makeText(
                    context,
                    "دریافت اطلاعات به مشکل خورده است دوباره تلاش کنین.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: IOException) {
            Log.d("downloadFile error", e.message.toString())

            e.printStackTrace()
        }

    }
}

