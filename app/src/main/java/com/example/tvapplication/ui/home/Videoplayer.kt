package com.example.tvapplication.ui.home

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.tvapplication.domain.VideoResultEntity


@OptIn(UnstableApi::class)
@SuppressLint("OpaqueUnitKey")
@ExperimentalAnimationApi
@Composable
fun VideoPlayer(
    mediaItems : List<MediaItem>,
    video: VideoResultEntity,
    playingIndex: State<Int>,
    onVideoChange: (Int) -> Unit,
    isVideoEnded: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Get the current context
    val context = LocalContext.current

    // Create a list of MediaItems for the ExoPlayer
//    val mediaItems = arrayListOf<MediaItem>()
//    mediaItems.add(
//        MediaItem.Builder().setUri(video.video).build()
//    )

    // Initialize ExoPlayer
    val exoPlayer = ExoPlayer.Builder(context).build().apply {
        this.setMediaItems(mediaItems)
        this.prepare()
        this.repeatMode=Player.REPEAT_MODE_ALL
//
//        this.addListener(object : Player.Listener {
//            override fun onEvents(player: Player, events: Player.Events) {
//                super.onEvents(player, events)
//
//                // Hide video title after playing for 200 milliseconds
////                    if (player.contentPosition >= 200) visible.value = false
//            }
//
//            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
//                super.onMediaItemTransition(mediaItem, reason)
//                // Callback when the video changes
//                onVideoChange(this@apply.currentPeriodIndex)
////                    visible.value = true
////                    videoTitle.value = mediaItem?.mediaMetadata?.displayTitle.toString()
//            }
//
//
//        })
    }
//    exoPlayer.addListener(object : Player.Listener {
//        override fun onPlaybackStateChanged(playbackState: Int) {
//
////            if (playbackState == Player.STATE_ENDED) {
////                // Play the next video
//////                        if (hasNextMediaItem())
////                exoPlayer.seekToNext()
////                exoPlayer.prepare()
////                exoPlayer.playWhenReady
////
////            }
//
//            val stateString: String = when (playbackState) {
//                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
//                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
//                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
//                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
//                else -> "UNKNOWN_STATE             -"
//            }
//            Log.d("TAG", "changed state to $stateString")
//        }
//    })


    // Seek to the specified index and start playing

    val index = playingIndex.value
//    exoPlayer.seekTo(playingIndex.value, C.TIME_UNSET)
    exoPlayer.prepare()
    exoPlayer.playWhenReady = true
//    exoPlayer.play()
    exoPlayer.repeatMode =Player.REPEAT_MODE_ALL
//    // Add a lifecycle observer to manage player state based on lifecycle events
//    LocalLifecycleOwner.current.lifecycle.addObserver(object : LifecycleEventObserver {
//        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//            when (event) {
//                Lifecycle.Event.ON_START -> {
//                    // Start playing when the Composable is in the foreground
//                    if (exoPlayer.isPlaying.not()) {
//                        exoPlayer.play()
//                    }
//                }
//
//                Lifecycle.Event.ON_STOP -> {
//                    // Pause the player when the Composable is in the background
//                    exoPlayer.pause()
//                }
//
//                else -> {
//                    // Nothing
//                }
//            }
//        }
//    })

//    exoPlayer.repeatMode =Player.REPEAT_MODE_ALL


    // Column Composable to contain the video player
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(end = 10.dp)
            .background(Color.Black)
    ) {
        // DisposableEffect to release the ExoPlayer when the Composable is disposed
        DisposableEffect(
            AndroidView(modifier = modifier.fillMaxSize(), factory = {
                // AndroidView to embed a PlayerView into Compose
                PlayerView(context).apply {
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    // Set resize mode to fill the available space
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
//                    setRepeatToggleModes( Player.REPEAT_MODE_ALL)
                    // Hide unnecessary player controls
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    setShowFastForwardButton(false)
                    setShowRewindButton(false)
                }
            })
        ) {
            // Dispose the ExoPlayer when the Composable is disposed
            onDispose {
                exoPlayer.release()
            }
        }
    }


}
