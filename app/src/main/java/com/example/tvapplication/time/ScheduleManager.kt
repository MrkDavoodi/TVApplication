package com.example.tvapplication.time

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.tvapplication.launcher.BootCompletedReceiver
import java.util.Calendar

fun setAlarm(broadcastReceiver:BroadcastReceiver,context: Context,day:Int,hour:Int,minute:Int) {

    val alarm = Intent(
        context,
        BootCompletedReceiver::class.java
    )
    alarm.setAction("gotoBlack")
//    val broadcastIntent = Intent("gotoBlack")
//
//    context.sendBroadcast(broadcastIntent)
    val alarmRunning = PendingIntent.getBroadcast(
        context,
        1234,
        alarm,
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    ) != null
    if (!alarmRunning) {
        val pendingIntent = PendingIntent.getBroadcast(context, 1234, alarm, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.DAY_OF_WEEK, day)

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {

            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

    }
}