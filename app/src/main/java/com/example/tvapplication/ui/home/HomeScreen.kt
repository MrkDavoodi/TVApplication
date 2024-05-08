package com.example.tvapplication.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tvapplication.commons.internet.ConnectionState
import com.example.tvapplication.commons.internet.connectivityState
import com.example.tvapplication.commons.mainService
import com.example.tvapplication.data.ApiURL
import com.example.tvapplication.data.local.SharedPreferencesHelper.getVersionNumber
import com.example.tvapplication.data.local.SharedPreferencesHelper.saveVersionNumber
import com.example.tvapplication.forTest.VideoItem
import com.example.tvapplication.ui.internet.NoConnectionScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(
    viewModel: VideoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val versionDetails by viewModel.versionDetails
    val connection by connectivityState()
    val videos: MutableList<VideoItem> = mutableListOf()


    val isConnected = connection === ConnectionState.Available

    if (isConnected) {
        mainService {
            viewModel.getVersion()
            Log.i("Tag", "call service !")
        }

        versionDetails.let {
            versionDetails?.VideoList?.forEach {video->
                videos.add(
                    VideoItem(
                        id = video.substringBefore("."),
                        mediaUrl = ApiURL.URL_VIDEO + video,
                        thumbnail = ""
                    )
                )
            }

            val oldVersionWeb = getVersionNumber(context)
            val newVersionWeb = versionDetails?.Ver
            if (newVersionWeb != null) {
                if (oldVersionWeb.toDouble() < newVersionWeb.toDouble())
                    saveVersionNumber(context, newVersionWeb.toString())
            }
            Toast.makeText(context, "" + getVersionNumber(context), Toast.LENGTH_LONG).show()
//            VideoList(videos = videos)
        }
    } else {
        NoConnectionScreen()

    }


}
