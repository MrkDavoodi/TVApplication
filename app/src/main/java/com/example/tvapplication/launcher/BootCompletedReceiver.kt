package com.example.tvapplication.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


open class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent("gotoBlack")

        context?.sendBroadcast(intent)


//        val message = "Hellooo, alrm worked ---- context is :$context"
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//
//        try {
//
//            val intent = context?.packageManager?.getLaunchIntentForPackage(context.packageName)
//            intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            context?.applicationContext?.startActivity(intent)
//            Log.i("In Receiver", "This is error ${context?.applicationContext}")
//
//
//        } catch (e: Exception) {
//            Log.i("In Receiver", "This is error ${e.message}")
//
//            val launchIntent = Intent(Intent.ACTION_MAIN)
//            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER)
//
//            val packageManager = context?.packageManager
//            val resolveInfo =
//                packageManager?.resolveActivity(launchIntent, PackageManager.GET_ACTIVITIES)
//
//            if (resolveInfo != null) {
//                val appPackage = resolveInfo.activityInfo.packageName
//                val mainActivityClass = resolveInfo.activityInfo.name
//
//                val newLaunchIntent = Intent(Intent.ACTION_MAIN)
//                newLaunchIntent.setClassName(appPackage, mainActivityClass)
//                newLaunchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                context.startActivity(newLaunchIntent)
//                Log.i("In Receiver", "This is pkg : $appPackage")
//            } else {
//                Log.i("In Receiver", "This is error ${e.message}")
//                // Handle case where no activity is resolved
//            }
//        }


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


}

