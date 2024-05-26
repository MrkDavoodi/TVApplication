package com.example.tvapplication.time

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.tvapplication.launcher.BootCompletedReceiver
import java.util.Calendar

fun setAlarm(
    context: Context,
    day: Int,
    hour: Int,
    minute: Int,
    requestCode: Int,
    isTurnOf: Boolean,
) {

    val alarm = Intent(
        context,
        BootCompletedReceiver::class.java
    )
    if (isTurnOf)
        alarm.setAction("gotoTurnOf")
    else
        alarm.setAction("gotoTurnOn")

    if (hour != -1 || minute != -1) {
        val pendingIntent = PendingIntent.getBroadcast(context, 1234, alarm,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.DAY_OF_WEEK, day)

        }
        val duration = calendar.timeInMillis-System.currentTimeMillis()
        if (duration>0)
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