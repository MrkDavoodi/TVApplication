package com.example.tvapplication.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tvapplication.commons.CircularIndeterminateProgressBar
import com.example.tvapplication.commons.getAdminPermission
import com.example.tvapplication.commons.getPermission
import com.example.tvapplication.commons.internet.downloadVideos
import com.example.tvapplication.commons.swapList
import com.example.tvapplication.commons.turnOffScreen
import com.example.tvapplication.model.video.VideoItem
import com.example.tvapplication.ui.video.ExoPlayerColumnAutoplayScreen
import com.example.tvapplication.ui.video.VideoViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    context: Context,
    viewModel: VideoViewModel = hiltViewModel(),
) {
    val versionDetails by viewModel.versionDetails
//    if (versionDetails != null) {
//        val videos by viewModel.videos.collectAsState(listOf())
//
//        val localList = remember { mutableStateListOf<VideoItem>() }
//        localList.swapList(viewModel.videosFromDB)
//        if (localList.isEmpty()) {
//            LaunchedEffect(Unit) {
//                downloadVideos(
//                    context = context,
//                    fileName = videos[0].id + ".mp4",
//                    downloadPath = videos[0].mediaUrl
//                )
//            }
//
//        }
//    }

    Log.i("in Receiver", "test for command in Home")
    getPermission(context)
//    getAdminPermission(context)
    Box {
        CircularIndeterminateProgressBar(isDisplayed = true)
        ExoPlayerColumnAutoplayScreen(viewModel)
    }

}
