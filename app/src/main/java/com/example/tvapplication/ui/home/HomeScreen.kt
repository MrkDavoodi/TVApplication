package com.example.tvapplication.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tvapplication.commons.CircularIndeterminateProgressBar
import com.example.tvapplication.ui.video.ExoPlayerColumnAutoplayScreen
import com.example.tvapplication.ui.video.VideoViewModel
import com.example.tvapplication.workerManager.worker.AlarmCheckerWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: VideoViewModel = hiltViewModel(),
    context: Context
) {
    val versionDetails by viewModel.versionDetails

    Log.i("in Receiver", "test for command in Home")


//    versionDetails?.onOffTimeSchedule?.forEach {
    //set turn on time

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 16)
        set(Calendar.MINUTE, 22)
        set(Calendar.SECOND, 0)
        set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)

    }
    val duration=calendar.timeInMillis-System.currentTimeMillis()
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .build()
    val data = Data.Builder()
    data.putBoolean("isTurnOf", true)
    val request = OneTimeWorkRequestBuilder<AlarmCheckerWorker>()
        .setInitialDelay(duration, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .setInputData(data.build())
        .build()

    WorkManager.getInstance(context).enqueue(request)

//    setAlarm(
//        context = context,
//        day = Calendar.SATURDAY,
//        hour = 14,
//        minute = 25,
//        requestCode = 68,
//        isTurnOf = false
//    )
    //set turn of time
//    setAlarm(
//        context = context,
//        day = Calendar.SATURDAY,
//        hour = 13,
//        minute = 50,
//        requestCode = 1240,
//        isTurnOf = true
//    )
//    setAlarm(
//        context,
//        day = getDayOfWeek(it.day),
//        hour = if (it.offTime1?.hour != null) it.offTime1.hour.toInt() else 0,
//        minute = if (it.offTime1?.minute != null) it.offTime1.minute.toInt() else 0,
//        requestCode = 1234,
//        isTurnOf = true
//    )
//    }
    Box {
        CircularIndeterminateProgressBar(isDisplayed = true)
        ExoPlayerColumnAutoplayScreen(viewModel)
    }

}


