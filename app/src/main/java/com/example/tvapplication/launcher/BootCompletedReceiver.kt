package com.example.tvapplication.launcher

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.tvapplication.MainActivity
import java.util.Calendar


class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val message = "Hellooo, alrm worked ----"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        val intent2 = Intent(context, MainActivity::class.java)
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent2)

//         if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
//            val i = Intent(context, MainActivity::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context?.startActivity(i)}
//
//        if (intent?.action == "EXIT_APP_ACTION") {
//            // Close all activities in the app
//            Toast.makeText(context, "Action: " + intent.action, Toast.LENGTH_SHORT).show()
////            exitProcess(0)
//            (context as? Activity)?.finishAffinity()
//        }
//        if(intent?.action == "Boot") {
//
//            val constraints = Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build()
//
//            val workRequest = OneTimeWorkRequestBuilder<AlarmCheckerWorker>()
//                .setConstraints(constraints)
//                .build()
//
//            val workManager = WorkManager.getInstance(context!!)
//            workManager.enqueueUniqueWork("openAppWork", ExistingWorkPolicy.REPLACE, workRequest)
//        }
    }
    fun setAlarm(context: Context) {
        Log.d("Carbon", "Alrm SET !!")

        // get a Calendar object with current time
        val cal: Calendar = Calendar.getInstance()
        // add 30 seconds to the calendar object
        cal.add(Calendar.SECOND, 60)
        val intent = Intent(context, BootCompletedReceiver::class.java)
        val sender =
            PendingIntent.getBroadcast(context, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Get the AlarmManager service
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),sender)
        }
//        am[AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()] = sender
    }
}

