package com.example.tvapplication.forTest

data class VideoItem(
    val id: String,
    val mediaUrl: String,
    val thumbnail: String,
    var lastPlayedPosition: Long = 0
)