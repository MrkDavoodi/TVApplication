package com.example.tvapplication.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tvapplication.launcher.ScreenOffReceiver
import com.example.tvapplication.ui.video.VideoViewModel


@Composable
fun HomeScreen(navController: NavController, viewModel: VideoViewModel = hiltViewModel()) {
    val versionDetails by viewModel.versionDetails

    val context = LocalContext.current
    Log.i("in Receiver","test for command in Home")
//    executeCommand("cmd activity start com.example.tvapplication")


    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    val scheduleServiceExecuterIntent: Intent = Intent(
        context,
        ScreenOffReceiver::class.java
    )

    scheduleServiceExecuterIntent.putExtra("state", "Main")
    val pendingIntent = PendingIntent.getBroadcast(context, 1048, scheduleServiceExecuterIntent, 0)
    val time=System.currentTimeMillis()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager?.set(AlarmManager.RTC_WAKEUP,60000,pendingIntent)
    }
//    alarmManager!!.setRepeating(
//        AlarmManager.RTC_WAKEUP,
//        time,
//        60000,
//        pendingIntent
//    )



//    versionDetails!!.onOffTimeSchedule.forEach{
//                setAlarm(broadcastReceiver,context,day= getDayOfWeek( it.day), hour = it.onTime1!!.hour.toInt(), minute = it.onTime1.minute.toInt())
//            }

//    Box {
//        CircularIndeterminateProgressBar(isDisplayed = true)
//        ExoPlayerColumnAutoplayScreen(viewModel)
//    }
}