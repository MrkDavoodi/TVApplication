package com.example.tvapplication.model.video

data class VideoItem(
    val id: String,
    val mediaUrl: String,
    val thumbnail: String,
    var lastPlayedPosition: Long = 0,
    var status: DownloadStatus = DownloadStatus.PENDING
)
enum class DownloadStatus {
    PENDING, DOWNLOADING, PAUSED, COMPLETED, ERROR
}