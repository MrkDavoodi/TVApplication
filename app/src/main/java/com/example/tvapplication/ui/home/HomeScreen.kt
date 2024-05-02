package com.example.tvapplication.ui.home

import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tvapplication.commons.mainService
import com.example.tvapplication.data.local.SharedPreferencesHelper.getVersionNumber
import com.example.tvapplication.data.local.SharedPreferencesHelper.saveVersionNumber
import com.example.tvapplication.video.Video
import com.example.tvapplication.video.VideoList


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
    val inputKeys: MutableList<Key> = mutableListOf()
    val secretKey = listOf(
        KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2,
        KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4
    )

//    Column(modifier = Modifier
//        .fillMaxSize()
//        .onKeyEvent {
//            inputKeys.add(it.key)
//
//            false
//        }) {

        VideoList(videos = allVideos)
//    }


}
