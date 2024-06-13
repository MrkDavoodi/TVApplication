package com.example.tvapplication.ui.video


import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.tvapplication.commons.HOLIDAY_CODE
import com.example.tvapplication.commons.deleteLocalFileIfNotExistInVideosFromRemote
import com.example.tvapplication.commons.getDayOfWeek
import com.example.tvapplication.commons.internet.ConnectionState
import com.example.tvapplication.commons.internet.connectivityState
import com.example.tvapplication.commons.internet.downloadVideos
import com.example.tvapplication.commons.mainService
import com.example.tvapplication.commons.swapList
import com.example.tvapplication.data.local.SharedPreferencesHelper
import com.example.tvapplication.model.version.VsersionModel
import com.example.tvapplication.model.video.VideoItem
import com.example.tvapplication.time.setAlarm
import com.example.tvapplication.ui.internet.NoConnectionScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import saman.zamani.persiandate.PersianDate
import java.io.File
import java.util.Calendar


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ExoPlayerColumnAutoplayScreen(viewModel: VideoViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val listState = rememberLazyListState()
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    val versionDetails by viewModel.versionDetails
    val videos by viewModel.videos.collectAsState(listOf())
    val items = remember { mutableStateListOf<VideoItem>() }

    var playingVideoItem by remember { mutableStateOf(items.firstOrNull()) }
    var isPlaying by remember { mutableStateOf(true) }
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available

    val localList : MutableList<VideoItem> = viewModel.videosFromDB
    if (localList.isNotEmpty()) {
        items.swapList(localList)

    }
    if (isConnected) {
//        mainService {
//            viewModel.getVersion()
//            Log.i("Tag", "call service !")
//
//        }
        if (versionDetails != null) {
            val oldVersionWeb = SharedPreferencesHelper.getVersionNumber(context).toDouble()
            val newVersionWeb = versionDetails?.Ver?.toDouble()
            SetSchedule(versionDetails, context)

            if (newVersionWeb != null) {
                if (localList.isEmpty() || oldVersionWeb < newVersionWeb || localList.size != videos.size) {
                    SharedPreferencesHelper.saveVersionNumber(
                        context,
                        versionDetails?.Ver.toString()
                    )

                    Toast.makeText(
                        context,
                        "" + SharedPreferencesHelper.getVersionNumber(context),
                        Toast.LENGTH_SHORT
                    ).show()
                    if (localList.isEmpty())
                        items.swapList(listOf(videos[0]))
//                    if (localList.isNotEmpty() && localList.size != videos.size) {
//                        localList.forEach { localItem ->
//                            videos.forEach {
//                                if (it.id != localItem.id) {
//                                    deleteLocalFileIfNotExistInVideosFromRemote(it.id)
//                                    items.remove(it)
//                                }
//
//                            }
//
//                        }
//
//
//                    }
                    LaunchedEffect(Unit) {
                        val downloadedFiles = mutableListOf<File?>()

                        videos.forEach { video ->
                            downloadVideos(
                                context = context,
                                fileName = video.id + ".mp4",
                                downloadPath = video.mediaUrl
                            ).let { file ->
                                downloadedFiles.add(file)
                            }

                        }
                        items.clear()
                        val listSwap: ArrayList<VideoItem> = arrayListOf()
                        downloadedFiles.forEach {
                            if (it != null) {
                                listSwap.add(VideoItem(it.name, it.path, ""))
                            }
                        }
                        items.swapList(listSwap)
//
                    }
                    viewModel.getListFiles()
                    items.swapList(viewModel.videosFromDB)
                }
            }

        }

    } else {
        NoConnectionScreen()


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
//                    isPlaying = false
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
            .fillMaxSize(),
        state = listState
    ) {
        items(items.size) { index ->
            val playingVideo = playingVideoItem?.id
            val newItem = items[index].id
            AutoPlayVideoCard(
                exoPlayer = exoPlayer,
                isPlaying = items[index].id == playingVideoItem?.id,
            )
        }
    }
}

@Composable
private fun SetSchedule(
    versionDetails: VsersionModel?,
    context: Context
) {
    val holiday = versionDetails?.Holyday
    var holidays: List<Int> = emptyList()

    if (holiday != null) {
        holidays = holiday.map { it.code }
    }
    val pDate = PersianDate()
    val today = pDate.dayInYear
    val isTodayHoliday: Boolean = holidays[today] == 48

    if (isTodayHoliday) {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK)
        setAlarm(
            context,
            day = calendar.get(Calendar.DAY_OF_WEEK),
            hour = 0,
            minute = 0,
            requestCode = HOLIDAY_CODE,
            isTurnOf = true
        )

    } else {
        var index = 0
        versionDetails?.onOffTimeSchedule?.forEach {
            //set turn on time
            index++
            val day = getDayOfWeek(it.day)
            val hour = it.onTime1?.hour?.toInt()
            val min = it.onTime1?.minute?.toInt()
            setAlarm(
                context,
                day = day,
                hour = if (it.onTime1?.hour != null) it.onTime1.hour.toInt() else -1,
                minute = if (it.onTime1?.minute != null) it.onTime1.minute.toInt() else -1,
                requestCode = 1395 + index,
                isTurnOf = false
            )
            setAlarm(
                context,
                day = day,
                hour = if (it.offTime1?.hour != null) it.offTime1.hour.toInt() else 0,
                minute = if (it.offTime1?.minute != null) it.offTime1.minute.toInt() else 0,
                requestCode = 1989 + index,
                isTurnOf = true
            )
            //set turn of time
//            setAlarm(
//                context = context,
//                day = Calendar.TUESDAY,
//                hour = 23,
//                minute = 47,
//                requestCode = 1240 + index,
//                isTurnOf = true
//            )
//            setAlarm(
//                context = context,
//                day = Calendar.TUESDAY,
//                hour = 14,
//                minute = 48,
//                requestCode = 1368 + index,
//                isTurnOf = false
//            )
//            setAlarm(
//                context = context,
//                day = Calendar.TUESDAY,
//                hour = 14,
//                minute = 49,
//                requestCode = 1395 + index + 123,
//                isTurnOf = true
//            )

        }
    }
}





