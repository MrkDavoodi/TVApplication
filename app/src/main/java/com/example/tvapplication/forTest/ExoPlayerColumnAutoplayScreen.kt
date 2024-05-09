package com.example.tvapplication.forTest


import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.tvapplication.commons.CircularIndeterminateProgressBar
import com.example.tvapplication.commons.internet.ConnectionState
import com.example.tvapplication.commons.internet.connectivityState
import com.example.tvapplication.commons.internet.saveFileData
import com.example.tvapplication.commons.swapList
import com.example.tvapplication.data.local.SharedPreferencesHelper
import com.example.tvapplication.time.setUpLaunchAndExitApp
import com.example.tvapplication.ui.home.VideoViewModel
import com.example.tvapplication.ui.internet.NoConnectionScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.math.abs


@kotlin.OptIn(ExperimentalCoroutinesApi::class)
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


//    ScheduleOnTime(context)
//    startOFFTimeAlarmManager(context)
    setUpLaunchAndExitApp(context)

    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available
    val localList = getListFiles(context)
    CircularIndeterminateProgressBar(isPlaying)

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

                        getListFiles(context).forEach {
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
//                    isPlaying=false

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
            // move playWhenReady to exoPlayer initialization if you don't
            // want to play next video automatically
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

private fun LazyListState.playingItem(videos: List<VideoItem>): VideoItem? {
    if (layoutInfo.visibleItemsInfo.isEmpty() || videos.isEmpty()) return null
    val layoutInfo = layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo
    val lastItem = visibleItems.last()
    val firstItemVisible = firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
    val itemSize = lastItem.size
    val itemOffset = lastItem.offset
    val totalOffset = layoutInfo.viewportEndOffset
    val lastItemVisible = lastItem.index == videos.size - 1 && totalOffset - itemOffset >= itemSize
    val midPoint = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
    val centerItems = visibleItems.sortedBy { abs(it.offset + it.size / 2 - midPoint) }

    return when {
        firstItemVisible -> videos.first()
        lastItemVisible -> videos.last()
        else -> centerItems.firstNotNullOf { videos[it.index] }
    }
}

@Composable
fun AutoPlayVideoCard(
    videoItem: VideoItem,
    isPlaying: Boolean,
    exoPlayer: ExoPlayer,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (isPlaying) {
            VideoPlayerWithControls(exoPlayer)
        } else {
            VideoThumbnail()
        }
        Text(
            text = videoItem.id,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerWithControls(exoPlayer: ExoPlayer) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                // Set resize mode to fill the available space
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                // Hide unnecessary player controls
                setShowNextButton(false)
                setShowPreviousButton(false)
                setShowFastForwardButton(false)
                setShowRewindButton(false)
                useController = false
            }
        },
        update = {
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true

        }
    )
}

@Composable
fun VideoThumbnail() {
}

fun getListFiles(context: Context): ArrayList<File> {
    val inFiles = arrayListOf<File>()
    try {
        val parentDir = File(context.filesDir.toString() + "/Folder")
        if (parentDir.exists()) {
            val files = parentDir.listFiles()?.filter { it.isFile }
            files?.forEach { file ->
                if (!inFiles.contains(file))
                    inFiles.add(file)
            } ?: println("No files found or directory does not exist.")

        }
    } catch (e: Exception) {
        Log.i("Exception Of File", e.message.toString())

    }
    return inFiles
}


