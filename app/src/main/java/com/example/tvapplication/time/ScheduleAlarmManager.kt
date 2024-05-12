package com.example.tvapplication.time

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Handler
import android.os.Looper
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleAlarmManager @Inject constructor(
    @ApplicationContext private val context: Context,
){
    private val handler = Handler(Looper.getMainLooper())

    fun scheduleTimeOn(alarmMgr: AlarmManager, alarmIntent: PendingIntent) {



// Set the alarm to start at 8:30 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 16)
            set(Calendar.MINUTE, 9)
        }

// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.
        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 20,
            alarmIntent
        )

    }
}