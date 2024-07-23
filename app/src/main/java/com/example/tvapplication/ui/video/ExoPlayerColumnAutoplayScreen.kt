package com.example.tvapplication.ui.video


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tvapplication.commons.HOLIDAY_CODE
import com.example.tvapplication.commons.getDayOfWeek
import com.example.tvapplication.commons.internet.ConnectionState
import com.example.tvapplication.commons.internet.connectivityState
import com.example.tvapplication.commons.internet.downloadVideos
import com.example.tvapplication.commons.swapList
import com.example.tvapplication.data.local.SharedPreferencesHelper
import com.example.tvapplication.model.version.VsersionModel
import com.example.tvapplication.model.video.VideoItem
import com.example.tvapplication.time.setAlarm
import com.example.tvapplication.ui.internet.NoConnectionScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import saman.zamani.persiandate.PersianDate
import java.util.Calendar


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ExoPlayerColumnAutoplayScreen(viewModel: VideoViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val versionDetails by viewModel.versionDetails
    val items = remember { mutableStateListOf<VideoItem>() }

    val videos by viewModel.videos.collectAsStateWithLifecycle()
    val videoList = mutableListOf<VideoItem?>(null)
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available
    viewModel.getLocalFiles()
    items.swapList(viewModel.videosFromDB)

    if (isConnected) {
//        mainService {
//            viewModel.getVersion()
//            Log.i("Tag", "call service !")
//
//        }
        if (versionDetails != null) {
            val oldVersionWeb = SharedPreferencesHelper(context).getDataFromSharPrf(
                SharedPreferencesHelper.VERSION_KEY, "0"
            ).toDouble()
            val newVersionWeb = versionDetails?.Ver?.toDouble()
//            SetSchedule(versionDetails, context)

            if (newVersionWeb != null) {
                if (items.isEmpty() || oldVersionWeb < newVersionWeb || items.size != videos.size) {
                    SharedPreferencesHelper(context).saveDataInSharPrf( SharedPreferencesHelper.VERSION_KEY,
                        versionDetails?.Ver.toString()
                    )
                    if (items.isEmpty()) {
                        items.swapList(listOf(VideoItem(videos[0].id, videos[0].mediaUrl, "")))
//                        LaunchedEffect(Unit) {
//                            downloadVideos(
//                                context = context,
//                                fileName = videos[0].id + ".mp4",
//                                downloadPath = videos[0].mediaUrl
//                            ).let { file ->
//                                if (file != null) {
//                                    items.swapList(listOf(VideoItem(file.name, file.path, "")))
////                                    localList.add(VideoItem(file.name, file.path, ""))
//                                }
//                            }
//                        }
                    }
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
                        val downloadedFiles = mutableListOf<VideoItem>()

                        videos.forEach { video ->
                            downloadVideos(
                                context = context,
                                fileName = video.id + ".mp4",
                                downloadPath = video.mediaUrl
                            ).let { file ->
                                if (file != null)
                                    downloadedFiles.add(
                                        VideoItem(
                                            file.name.substringBefore("."),
                                            file.path,
                                            ""
                                        )
                                    )
                            }

                        }
//                        items.swapList(downloadedFiles)
                        viewModel.getLocalFiles()
                        items.swapList(viewModel.videosFromDB)
                    }
                }
            }

        }

    } else {
        NoConnectionScreen()
    }

    ExoPlayerAutoplayScreen(items)
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
            val hour2 = it.offTime1?.hour?.toInt()
            val min2 = it.offTime1?.minute?.toInt()
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
                requestCode = System.currentTimeMillis().toInt(),
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





