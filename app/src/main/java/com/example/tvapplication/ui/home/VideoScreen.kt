package com.example.tvapplication.ui.home

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import com.example.tvapplication.domain.VideoEntity

@Composable
fun VideoScreen(
    onVideoPressed: (VideoEntity) -> Unit,
    viewModel: VideoViewModel = hiltViewModel()
) {
    val item by viewModel.uiVideosState.collectAsState()
    val videos = item.videos
    val playingIndex = remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        LazyRow(
            horizontalArrangement =  Arrangement.Center,
            verticalAlignment =  Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize().layoutId(VIDEO_LAZY_COLUMN_ID)
        ) {
            val mediaItems = arrayListOf<MediaItem>()


            items(videos.size) { index ->
                playingIndex.intValue = index
                mediaItems.add(
                    MediaItem.Builder().setUri(videos[index].videoResultEntity.video).build()
                )
                VideoItem(mediaItems,videos[index], onItemClick = { data ->
                    onVideoPressed.invoke(data)
                    Log.d("Video pressed: ", data.name)
                }, playingIndex)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VideoItem(
    mediaItems : List<MediaItem>,
    video: VideoEntity,
    onItemClick: (VideoEntity) -> Unit,
    playingIndex: MutableState<Int>
) {
    fun onTrailerChange(index: Int) {
        playingIndex.value = index
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onItemClick.invoke(video)
            }
            .background(color = Color.DarkGray),
        verticalArrangement = Arrangement.Center,
    ) {
        VideoPlayer(
            mediaItems = mediaItems,
            video = video.videoResultEntity,
            playingIndex = playingIndex,
            onVideoChange = { newIndex -> onTrailerChange(newIndex) },
            isVideoEnded = {

            }
        )
    }
}

const val NAME_ID = "video_name_txt"
const val DESCRIPTION_ID = "video_description_id"
const val VIDEO_COLUMN_ID = "video_column"
const val VIDEO_LAZY_COLUMN_ID = "video_lazy_column"

@ExperimentalAnimationApi
@Preview()
@Composable
fun ShowVideoPreview() {

    VideoScreen(onVideoPressed = {})

}