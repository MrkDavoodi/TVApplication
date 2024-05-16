package com.example.tvapplication.time

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import com.example.tvapplication.data.local.alarm.WorkRequestManager
import com.example.tvapplication.launcher.BootCompletedReceiver
import com.example.tvapplication.workerManager.worker.ALARM_CHECKER_TAG
import com.example.tvapplication.workerManager.worker.AlarmCheckerWorker
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
fun setUpLaunchApp(context: Context, workRequestManager: WorkRequestManager,){
    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(context, BootCompletedReceiver::class.java).apply {
        action="MY_BOOT"
    }
    val alarmPendingIntent = PendingIntent.getBroadcast(
        context,
        1,
        alarmIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )
    val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 36)
        set(Calendar.SECOND, 0)
        if (timeInMillis <= System.currentTimeMillis()) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    workRequestManager.enqueueWorker<AlarmCheckerWorker>(ALARM_CHECKER_TAG)


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmPendingIntent,
        )
    }    else {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmPendingIntent
        )

    }


//    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//    val intent = Intent(context, BootCompletedReceiver::class.java).apply {
//        action = "BOOT_COMPLETED"
//    }
//    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
//        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE )
//
//    val calendar = Calendar.getInstance()
//    calendar.timeInMillis = System.currentTimeMillis()
//    calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY) // Set the day of the week
//    calendar.set(Calendar.HOUR_OF_DAY, 13) // Set the hour
//    calendar.set(Calendar.MINUTE, 16) // Set the minute
//
//// Set the alarm to trigger on specific days and times
////    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            pendingIntent,
//        )
//    }
//    else {
//        alarmManager.setExact(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            pendingIntent
//        )
//
//    }


}