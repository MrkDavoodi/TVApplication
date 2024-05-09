package com.example.tvapplication.time

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import java.util.Calendar

@Composable
fun ScheduleOnTime(context:Context){
    val alarmMgr: AlarmManager?

    alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent: PendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
        PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 16)
    }

    alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)

// setRepeating() lets you specify a precise custom interval--in this case,
// 1 day.
//    alarmMgr.setRepeating(
//        AlarmManager.RTC_WAKEUP,
//        calendar.timeInMillis,
//        1000 * 60 * 60 * 24,
//        alarmIntent
//    )
}