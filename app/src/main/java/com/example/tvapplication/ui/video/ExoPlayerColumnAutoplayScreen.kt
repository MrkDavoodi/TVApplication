package com.example.tvapplication.ui.video


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.tvapplication.commons.internet.ConnectionState
import com.example.tvapplication.commons.internet.connectivityState
import com.example.tvapplication.commons.internet.saveFileData
import com.example.tvapplication.commons.swapList
import com.example.tvapplication.data.local.SharedPreferencesHelper
import com.example.tvapplication.model.video.VideoItem
import com.example.tvapplication.ui.internet.NoConnectionScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ExoPlayerColumnAutoplayScreen(viewModel: VideoViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val listState = rememberLazyListState()
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    val videos by viewModel.videos.collectAsState()
    val versionDetails by viewModel.versionDetails
    val videoList: MutableList<VideoItem> = mutableListOf()
    val items = remember { mutableStateListOf<VideoItem>() }
    var playingVideoItem by remember { mutableStateOf(items.firstOrNull()) }
    var isPlaying by remember {mutableStateOf(true)}
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available
    val localList = viewModel.getListFiles(context)


    if (isConnected) {
//        mainService {
        viewModel.getVersion()
        Log.i("Tag", "call service !")

//        }
        if (versionDetails != null) {
            val oldVersionWeb = SharedPreferencesHelper.getVersionNumber(context)
            val newVersionWeb = versionDetails?.Ver
            if (newVersionWeb != null) {
                if (localList.isEmpty() || oldVersionWeb.toDouble() < newVersionWeb.toDouble()) {
                    SharedPreferencesHelper.saveVersionNumber(
                        context,
                        versionDetails?.Ver.toString()
                    )
                    LaunchedEffect(null) {
                        runBlocking {
                            videos.forEach { video ->
                                async {
                                    saveFileData(
                                        context = context,
                                        url = video.mediaUrl,
                                        fileName = video.id
                                    )
                                }.await()
                            }
                        }

                        viewModel.getListFiles(context).forEach {
                            videoList.add(
                                VideoItem(
                                    id = it.name.substringBefore("."),
                                    mediaUrl = it.path,
                                    thumbnail = ""
                                )
                            )
                        }

                        items.swapList(videoList)
                    }

                } else {
                    localList.forEach {
                        videoList.add(
                            VideoItem(
                                id = it.name.substringBefore("."),
                                mediaUrl = it.path,
                                thumbnail = ""
                            )
                        )
                    }
                    items.swapList(videoList)
                }
            }

        }

        Toast.makeText(
            context,
            "" + SharedPreferencesHelper.getVersionNumber(context),
            Toast.LENGTH_LONG
        ).show()
    } else {
        if (localList.isNotEmpty()) {
            localList.forEach {
                videoList.add(
                    VideoItem(
                        id = it.name.substringBefore("."),
                        mediaUrl = it.path,
                        thumbnail = ""
                    )
                )
            }
            items.swapList(videoList)


        } else {
            NoConnectionScreen()
        }

    }

    LaunchedEffect(Unit) {
        snapshotFlow {
            listState.playingItem(items)
        }.collect { videoItem ->
            playingVideoItem = videoItem
        }
    }

    LaunchedEffect(playingVideoItem) {
        // is null only upon entering the screen
        if (playingVideoItem == null) {
            exoPlayer.pause()
        } else {
            for (i in items.indices) {
                val songPath: String = items[i].mediaUrl
                val item: MediaItem = MediaItem.fromUri(songPath)
                exoPlayer.addMediaItem(item)
            }
            exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        }
    }

    DisposableEffect(exoPlayer) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (playingVideoItem == null) return@LifecycleEventObserver
            when (event) {
                Lifecycle.Event.ON_START -> {
                    isPlaying=false
                    exoPlayer.play()
                }
                Lifecycle.Event.ON_STOP -> exoPlayer.pause()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            exoPlayer.release()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        state = listState
    ) {
        items(items.size) { index ->
            AutoPlayVideoCard(
                videoItem = items[index],
                exoPlayer = exoPlayer,
                isPlaying = items[index].id == playingVideoItem?.id,
            )
        }
    }
}





