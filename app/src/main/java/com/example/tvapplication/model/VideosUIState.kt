package com.example.tvapplication.model

import com.example.tvapplication.domain.VideoEntity

data class VideosUIState(
    val isLoading: Boolean = false,
    val videos: List<VideoEntity> = listOf(),
    val error: Error = Error()
)