package com.example.tvapplication.data.local.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.tvapplication.commons.GlobalProperties.PendingIntentFlags
import com.example.tvapplication.commons.getDayOfWeek
import com.example.tvapplication.launcher.AlarmBroadcastReceiver
import com.example.tvapplication.model.version.OnOffTimeSchedule
import com.example.tvapplication.workerManager.worker.ALARM_CHECKER_TAG
import com.example.tvapplication.workerManager.worker.AlarmCheckerWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleAlarmManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val workRequestManager: WorkRequestManager,
) {

    private val handler = Handler(Looper.getMainLooper())

    fun schedule(alarm: OnOffTimeSchedule) {
        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(applicationContext, AlarmBroadcastReceiver::class.java).apply {
            action = ""
        }
        val alarmOffTimeIntent = Intent(applicationContext, AlarmBroadcastReceiver::class.java).apply {
            action = "EXIT_APP_ACTION"
        }
        val alarmPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            alarmIntent,
            PendingIntentFlags,
        )
        val calendar = Calendar.getInstance().apply {
            alarm.onTime1?.hour?.toInt()?.let { set(Calendar.HOUR_OF_DAY, it) }
            alarm.onTime1?.minute?.toInt()?.let { set(Calendar.MINUTE, it) }
            set(Calendar.SECOND, 0)
            set(Calendar.DAY_OF_WEEK, getDayOfWeek(alarm.day))

        }
        val toastText =
            "Recurring Alarm scheduled at ${alarm.onTime1?.hour}:${alarm.onTime1?.minute}"

        handler.post {
            Toast.makeText(applicationContext, toastText, Toast.LENGTH_SHORT).show()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmPendingIntent,
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmPendingIntent
            )

        }

    }

    fun cancel(alarm: OnOffTimeSchedule) {
        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alamIntent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            alamIntent,
            PendingIntentFlags,
        )
        val toastText = "Alarm canceled for ${alarm.onTime1?.hour}:${alarm.onTime1?.minute}"

        workRequestManager.enqueueWorker<AlarmCheckerWorker>(ALARM_CHECKER_TAG)

        handler.post {
            Toast.makeText(applicationContext, toastText, Toast.LENGTH_SHORT).show()
        }

        alarmManager.cancel(alarmPendingIntent)
    }

    suspend fun clearScheduledAlarms(alarmsList: List<OnOffTimeSchedule>) {
        alarmsList.asFlow().buffer().collect {
            cancel(it)

        }
    }
}