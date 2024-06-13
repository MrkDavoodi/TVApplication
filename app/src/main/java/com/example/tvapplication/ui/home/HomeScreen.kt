package com.example.tvapplication.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tvapplication.commons.CircularIndeterminateProgressBar
import com.example.tvapplication.commons.getAdminPermission
import com.example.tvapplication.commons.getPermission
import com.example.tvapplication.commons.turnOffScreen
import com.example.tvapplication.ui.video.ExoPlayerColumnAutoplayScreen
import com.example.tvapplication.ui.video.VideoViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    context: Context,
    viewModel: VideoViewModel = hiltViewModel(),
) {
//    viewModel.getVersion()
    val versionDetails by viewModel.versionDetails

    Log.i("in Receiver", "test for command in Home")
    getPermission(context)
//    getAdminPermission(context)
    Box {
        CircularIndeterminateProgressBar(isDisplayed = true)
        ExoPlayerColumnAutoplayScreen(viewModel)
    }

}
