package com.example.tvapplication.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tvapplication.commons.CircularIndeterminateProgressBar
import com.example.tvapplication.launcher.ScreenOffReceiver
import com.example.tvapplication.ui.video.ExoPlayerColumnAutoplayScreen
import com.example.tvapplication.ui.video.VideoViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: VideoViewModel = hiltViewModel()) {
    val versionDetails by viewModel.versionDetails

    val context = LocalContext.current


    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val screenOffIntent = Intent(context, ScreenOffReceiver::class.java)
    screenOffIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    screenOffIntent.setAction("onTime")
    val alarmPendingIntent = PendingIntent.getBroadcast(context, 0, screenOffIntent, 0)

    val triggerAtMillis = System.currentTimeMillis() + 60000 // 1 minutes in milliseconds
    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmPendingIntent)

//    versionDetails!!.onOffTimeSchedule.forEach{
//                setAlarm(broadcastReceiver,context,day= getDayOfWeek( it.day), hour = it.onTime1!!.hour.toInt(), minute = it.onTime1.minute.toInt())
//            }

    Box {
        CircularIndeterminateProgressBar(isDisplayed = true)
        ExoPlayerColumnAutoplayScreen(viewModel)
    }
}