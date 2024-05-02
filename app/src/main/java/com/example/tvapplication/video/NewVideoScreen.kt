package com.example.tvapplication.video

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.tvapplication.data.ApiURL.URL_VIDEO

data class Video(val uri: String)


@kotlin.OptIn(ExperimentalAnimationApi::class)
@Composable
fun VideoList(videos: List<Video>) {
    val context = LocalContext.current
    var mediaItemIndex = 0
    var playbackPosition = 0L
    val mediaList: MutableList<MediaItem> = mutableListOf()
    videos.forEach{
        mediaList.add(MediaItem.fromUri(URL_VIDEO+it.uri))
    }



    LazyRow {
        items(mediaList.size) { video ->
            VideoItemNew(context,video = mediaList[video],mediaList)
        }
    }
}

@OptIn(UnstableApi::class)
@SuppressLint("OpaqueUnitKey")
@ExperimentalAnimationApi
@Composable
fun VideoItemNew(context: Context, video: MediaItem,mediaList :List<MediaItem> ) {

    val exoPlayer = remember { ExoPlayer.Builder(context).build().apply {  }  }
    exoPlayer.setMediaItems(mediaList)
    exoPlayer.playWhenReady = true
    exoPlayer.prepare()
    exoPlayer.repeatMode = Player.REPEAT_MODE_ALL

    LocalLifecycleOwner.current.lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_START -> {
                    // Start playing when the Composable is in the foreground
                    if (exoPlayer.isPlaying.not()) {
                        exoPlayer.play()
                    }
                }

                Lifecycle.Event.ON_STOP -> {
                    // Pause the player when the Composable is in the background
                    exoPlayer.pause()
                }

                else -> {
                    // Nothing
                }
            }
        }
    })





    DisposableEffect(
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
                    setRepeatToggleModes(Player.REPEAT_MODE_ALL)
                    // Hide unnecessary player controls
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    setShowFastForwardButton(false)
                    setShowRewindButton(false)
                }
            },
            update = {
                exoPlayer.setMediaItem(video)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true

            }
        )) {
        // Dispose the ExoPlayer when the Composable is disposed
        onDispose {
            exoPlayer.release()
        }
    }
}