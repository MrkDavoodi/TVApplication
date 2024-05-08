package com.example.tvapplication.time

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.tvapplication.MainActivity
import java.util.Calendar

@SuppressLint("SimpleDateFormat")

fun startAlarmManager(context:Context){

     var alarmMgr: AlarmManager? = null
     lateinit var alarmIntent: PendingIntent

    alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmIntent = Intent(context, MainActivity::class.java).let { intent ->
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

// Set the alarm to start
    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY)
        set(Calendar.HOUR_OF_DAY, 17)
        set(Calendar.MINUTE, 4)
    }

// setRepeating() lets you specify a precise custom interval--in this case,
// 1 day.
    alarmMgr.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        1000 * 60 * 60 * 24,
        alarmIntent
    )
}