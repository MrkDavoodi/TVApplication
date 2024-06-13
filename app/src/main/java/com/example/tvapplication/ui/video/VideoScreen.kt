package com.example.tvapplication.ui.video

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.tvapplication.model.video.VideoItem
import kotlin.math.abs

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
fun AutoPlayVideoCard(
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
        }
    }
}

fun LazyListState.playingItem(videos: List<VideoItem>): VideoItem? {
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


@ExperimentalAnimationApi
@Preview()
@Composable
fun ShowVideoPreview() {


}