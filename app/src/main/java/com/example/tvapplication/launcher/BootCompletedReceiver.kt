package com.example.tvapplication.launcher

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.tvapplication.MainActivity
import java.util.Calendar


class BootCompletedReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = "Hellooo, alrm worked ---- context is :$context"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()


//        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            val appPackage = "com.example.tvapplication.MainActivity" // Replace with your app's package name
            val mainActivityClass = "MainActivity" // Replace with your app's main activity class
try {
        val intent1 = Intent(context, MainActivity::class.java)
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent1)
        Log.i("In Receiver","This is context : $context")


}catch (e:Exception){
    Log.i("In Receiver","This is error ${e.message}")

        val launchIntent = Intent(Intent.ACTION_MAIN)
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val packageManager = context?.packageManager
        val resolveInfo = packageManager?.resolveActivity(launchIntent, PackageManager.GET_ACTIVITIES)

        if (resolveInfo != null) {
            val appPackage = resolveInfo.activityInfo.packageName
            val mainActivityClass = resolveInfo.activityInfo.name

            val newLaunchIntent = Intent(Intent.ACTION_MAIN)
            newLaunchIntent.setClassName(appPackage, mainActivityClass)
            newLaunchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(newLaunchIntent)
            Log.i("In Receiver","This is pkg : $appPackage")
        } else {
            Log.i("In Receiver","This is error ${e.message}")
            // Handle case where no activity is resolved
        }
}







//        }

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

