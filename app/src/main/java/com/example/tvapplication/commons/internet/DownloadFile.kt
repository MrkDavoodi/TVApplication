package com.example.tvapplication.commons.internet

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


suspend fun downloadFile(url: String, dest: String) {
    withContext(Dispatchers.IO) {
        URL(url).openStream()
    }.use { input ->
        File(dest).outputStream().use { output ->
            input.copyTo(output)
        }
    }
}

suspend fun saveFileData(context: Context, url: String, fileName: String) {

    var videoFilePath = ""
    val okHttpClient = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .build()
    Log.d("saving", request.toString())

    return withContext(Dispatchers.IO) {
        try {
            val response: Response = okHttpClient.newCall(request).execute()
            Log.d("response", response.toString())
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
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

