package com.example.tvapplication.time

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.tvapplication.MainActivity
import java.util.Calendar

@SuppressLint("SimpleDateFormat")

fun startOFFTimeAlarmManager(context:Context){
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, ExitAppReceiver::class.java).apply {
        action = "EXIT_APP_ACTION"
    }

    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
    pendingIntent.send()

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY) // Set the day of the week
    calendar.set(Calendar.HOUR_OF_DAY, 13) // Set the hour
    calendar.set(Calendar.MINUTE, 31) // Set the minute

// Set the alarm to trigger on specific days and times
    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
}
fun intentToActivity(context: Context){
    val intent: Intent = Intent(context, MainActivity::class.java)
// Creating a pending intent and wrapping our intent
    val pendingIntent =
        PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    try {
        // Perform the operation associated with our pendingIntent
        pendingIntent.send()
    } catch (e: PendingIntent.CanceledException) {
        e.printStackTrace()
    }
}

@SuppressLint("ScheduleExactAlarm")
fun setUpLaunchAndExitApp(context: Context){
    // Set up alarms for launching the app multiple times in a day
    val launchTime1 = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 13) // Set the first launch time (for example, 9 AM)
        set(Calendar.MINUTE,44) // Set the first launch time (for example, 9 AM)
    }.timeInMillis

    val launchTime2 = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 13) // Set the second launch time (for example, 12 PM)
        set(Calendar.MINUTE, 48) // Set the second launch time (for example, 12 PM)
    }.timeInMillis

// Schedule the app launch alarms using AlarmManager
    val launchIntent = Intent(context, AlarmReceiver::class.java)
    val launchPendingIntent1 = PendingIntent.getBroadcast(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val launchPendingIntent2 = PendingIntent.getBroadcast(context, 1, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, launchTime1, launchPendingIntent1)
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, launchTime2, launchPendingIntent2)



}