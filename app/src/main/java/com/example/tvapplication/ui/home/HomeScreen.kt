package com.example.tvapplication.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tvapplication.commons.CircularIndeterminateProgressBar
import com.example.tvapplication.commons.getDayOfWeek
import com.example.tvapplication.time.setAlarm
import com.example.tvapplication.ui.video.ExoPlayerColumnAutoplayScreen
import com.example.tvapplication.ui.video.VideoViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: VideoViewModel = hiltViewModel(),
    context: Context
) {
    viewModel.getVersion()
    val versionDetails by viewModel.versionDetails

    Log.i("in Receiver", "test for command in Home")

    var index = 0
    versionDetails?.onOffTimeSchedule?.forEach {
        //set turn on time
        index++
        setAlarm(
            context,
            day = getDayOfWeek(it.day),
            hour = if (it.onTime1?.hour != null) it.onTime1.hour.toInt() else 0,
            minute = if (it.onTime1?.minute != null) it.onTime1.minute.toInt() else 0,
            requestCode = 168 + index,
            isTurnOf = false
        )
        //set turn of time
//                    setAlarm(
//                        context = context,
//                        day = Calendar.SUNDAY,
//                        hour = 12,
//                        minute = 53,
//                        requestCode = 1240+index,
//                        isTurnOf = true
//                    )
        setAlarm(
            context,
            day = getDayOfWeek(it.day),
            hour = if (it.offTime1?.hour != null) it.offTime1.hour.toInt() else 0,
            minute = if (it.offTime1?.minute != null) it.offTime1.minute.toInt() else 0,
            requestCode = 1234 + index,
            isTurnOf = true
        )
    }


    Box {
        CircularIndeterminateProgressBar(isDisplayed = true)
        ExoPlayerColumnAutoplayScreen(viewModel)
    }

}


