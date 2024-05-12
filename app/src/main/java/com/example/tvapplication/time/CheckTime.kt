package com.example.tvapplication.time

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.tvapplication.launcher.BootCompletedReceiver
import java.util.Calendar


@SuppressLint("SimpleDateFormat")

fun startOFFTimeAlarmManager(context:Context){
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, BootCompletedReceiver::class.java).apply {
        action = "EXIT_APP_ACTION"
    }
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY) // Set the day of the week
    calendar.set(Calendar.HOUR_OF_DAY, 13) // Set the hour
    calendar.set(Calendar.MINUTE, 25) // Set the minute

// Set the alarm to trigger on specific days and times
    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
}


@SuppressLint("ScheduleExactAlarm")
fun setUpLaunchApp(context: Context){
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, BootCompletedReceiver::class.java).apply {
        action = "ACTION_BOOT_COMPLETED"
    }
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY) // Set the day of the week
    calendar.set(Calendar.HOUR_OF_DAY, 13) // Set the hour
    calendar.set(Calendar.MINUTE, 27) // Set the minute

// Set the alarm to trigger on specific days and times
    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)


}