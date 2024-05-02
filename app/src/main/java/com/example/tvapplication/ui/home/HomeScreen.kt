package com.example.tvapplication.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tvapplication.commons.mainService
import com.example.tvapplication.data.local.SharedPreferencesHelper.getVersionNumber
import com.example.tvapplication.data.local.SharedPreferencesHelper.saveVersionNumber
import com.example.tvapplication.video.Video
import com.example.tvapplication.video.VideoList


@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(
    viewModel: VideoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val versionDetails by viewModel.versionDetails
    val allVideos = arrayListOf<Video>()

    mainService {
        viewModel.getVersion()
        Log.i("Tag", "call service !")
    }

    versionDetails.let {
        val list = versionDetails?.VideoList
        if (list != null) {
            for (i in list) {
                allVideos.add(Video(i))

            }
        }
        val oldVersionWeb = getVersionNumber(context)
        val newVersionWeb = versionDetails?.Ver
        if (newVersionWeb != null) {
            if (oldVersionWeb.toDouble() < newVersionWeb.toDouble())
                saveVersionNumber(context, versionDetails?.Ver.toString())
        }
        Toast.makeText(context, "" + getVersionNumber(context), Toast.LENGTH_LONG).show()
    }

    VideoList(videos = allVideos)


}
